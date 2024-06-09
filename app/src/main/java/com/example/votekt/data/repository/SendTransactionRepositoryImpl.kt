package com.example.votekt.data.repository

import android.util.Log
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.errors.RequestFailedException
import by.alexandr7035.ethereum.errors.RequestNotExecutedException
import by.alexandr7035.ethereum.model.EthTransactionEstimation
import by.alexandr7035.ethereum.model.GWEI
import by.alexandr7035.ethereum.model.Wei
import com.cioccarellia.ksprefs.KsPrefs
import com.example.votekt.BuildConfig
import com.example.votekt.data.cache.PrefKeys
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.transactions.PrepareTransactionData
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.TransactionEstimationError
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.transactions.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.kethereum.eip1559.signer.signViaEIP1559
import org.kethereum.extensions.transactions.encodeAsEIP1559Tx
import org.kethereum.model.ECKeyPair
import org.kethereum.model.Transaction
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.toHexString

class SendTransactionRepositoryImpl(
    private val ethereumClient: EthereumClient,
    private val cryptoHelper: CryptoHelper,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val proposalsDao: ProposalsDao,
    private val ksPrefs: KsPrefs,
) : SendTransactionRepository {

    private var transaction: Transaction = createEmptyTransaction()

    private val _state: MutableStateFlow<ReviewTransactionData?> = MutableStateFlow(null)
    override val state = _state

    override suspend fun requirePrepareTransaction(data: PrepareTransactionData) = withContext(Dispatchers.IO) {
        Log.d(LOG_TAG, "require prepare transaction $data")
        try {
            val selfAddress = accountRepository.getSelfAddress()

            val recipientAddress = when (data) {
                is PrepareTransactionData.SendValue -> data.receiver
                is PrepareTransactionData.ContractInteraction -> data.contractAddress
            }

            transaction = createEmptyTransaction().apply {
                this.from = selfAddress
                this.to = recipientAddress
                this.chain = BuildConfig.CHAIN_ID.toBigInteger()
                this.value = data.value.value
                this.input = if (data is PrepareTransactionData.ContractInteraction) {
                    data.contractInput.value
                } else {
                    ByteArray(0)
                }
            }

            // Initial state
            _state.update {
                ReviewTransactionData(
                    data = data,
                    transactionType = data.mapTransactionType(),
                    to = recipientAddress,
                    value = data.value,
                    input = if (data is PrepareTransactionData.ContractInteraction) {
                        data.contractInput.value.toHexString()
                    } else {
                        null
                    },
                    totalEstimatedFee = null,
                    minerTipFee = null,
                    estimationError = null,
                )
            }

            val transactionEstimation = ethereumClient.estimateTransaction(transaction)
            transaction.applyGasEstimation(transactionEstimation)
        } catch (e: Exception) {
            reduceTransactionError(e)
        }
    }

    private fun PrepareTransactionData.mapTransactionType() = when (this) {
        is PrepareTransactionData.ContractInteraction.CreateProposal -> TransactionType.CREATE_PROPOSAL
        is PrepareTransactionData.ContractInteraction.VoteOnProposal -> TransactionType.VOTE
        is PrepareTransactionData.SendValue -> TransactionType.PAYMENT
    }

    private fun reduceTransactionError(e: Exception) {
        when (e) {
            is RequestFailedException -> {
                _state.update { it?.copy(estimationError = TransactionEstimationError.ExecutionError(e.error)) }
            }

            is RequestNotExecutedException -> {
                _state.update { it?.copy(estimationError = TransactionEstimationError.ExecutionError(e.error)) }
            }

            else -> {
                _state.update { it?.copy(estimationError = TransactionEstimationError.ExecutionError(e.message)) }
            }
        }
    }

    override suspend fun confirmTransaction() = withContext(Dispatchers.IO) {
        try {
            val currentState = _state.value ?: return@withContext

            val credentials = loadCredentials()
            val signedTxData = transaction.signAndEncode(credentials)
            val hash = ethereumClient.sendRawTransaction(signedTxData)

            cacheLocalTransactionData(currentState.data, hash)

            _state.update { null }
        } catch (e: Exception) {
            reduceTransactionError(e)
        }
    }

    override suspend fun cancelTransaction() {
        transaction = createEmptyTransaction()
        state.update { null }
    }

    private fun Transaction.applyGasEstimation(estimation: EthTransactionEstimation) {
        val currentState = _state.value ?: return

        val baseFee = estimation.gasPrice
        val maxPriorityFeePerGas = DEFAULT_MINER_TIP
        // Can set more in future
        val maxFeePerGas = baseFee + maxPriorityFeePerGas

        this.apply {
            this.gasLimit = estimation.estimatedGas
            this.maxPriorityFeePerGas = maxPriorityFeePerGas.value
            this.maxFeePerGas = maxFeePerGas.value
            this.nonce = estimation.nonce
        }

        Log.d(LOG_TAG, "estimated tx $this")
        Log.d(LOG_TAG, "contract input: ${this.input.toHexString()}")

        val totalFee = Wei(estimation.estimatedGas * maxFeePerGas.value)

        val insufficientBalanceError = if (estimation.balance < totalFee) {
            TransactionEstimationError.InsufficientBalance
        } else {
            null
        }

        _state.update {
            currentState.copy(
                totalEstimatedFee = totalFee,
                minerTipFee = maxPriorityFeePerGas,
                estimationError = insufficientBalanceError
            )
        }
    }

    private fun Transaction.signAndEncode(credentials: ECKeyPair): String {
        val signature = this.signViaEIP1559(credentials)
        Log.d(LOG_TAG, "sign tx $this")
        return this.encodeAsEIP1559Tx(signature).toHexString()
    }

    private suspend fun cacheLocalTransactionData(
        prepareTransactionData: PrepareTransactionData,
        transactionHash: String
    ) = withContext(Dispatchers.IO) {
        transactionRepository.addNewTransaction(
            transactionHash = TransactionHash(transactionHash),
            transactionType = prepareTransactionData.transactionType,
            value = prepareTransactionData.value,
        )

        when (prepareTransactionData) {
            is PrepareTransactionData.ContractInteraction.CreateProposal -> {
                proposalsDao.updateDeployTransactionHash(
                    proposalId = prepareTransactionData.proposalUuid,
                    newDeployTransactionHash = transactionHash
                )
            }

            is PrepareTransactionData.ContractInteraction.VoteOnProposal -> {
                proposalsDao.updateSelfVote(
                    proposalNumber = prepareTransactionData.proposalNumber,
                    supported = prepareTransactionData.vote,
                    voteTransactionHash = transactionHash,
                )
            }

            else -> {}
        }
    }

    private suspend fun loadCredentials(): ECKeyPair = withContext(Dispatchers.IO) {
        val phrase = ksPrefs.pull<String>(PrefKeys.ACCOUNT_MNEMONIC_PHRASE)
        return@withContext cryptoHelper.generateCredentialsFromMnemonic(phrase).ecKeyPair!!
    }

    companion object {
        val DEFAULT_MINER_TIP = 1.5.GWEI
        const val LOG_TAG = "SEND_TX_TAG"
    }
}
