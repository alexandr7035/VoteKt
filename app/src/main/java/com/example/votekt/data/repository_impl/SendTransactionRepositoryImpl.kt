package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.EthTransactionEstimation
import by.alexandr7035.ethereum.model.GWEI
import by.alexandr7035.ethereum.model.Wei
import cash.z.ecc.android.bip39.Mnemonics
import com.cioccarellia.ksprefs.KsPrefs
import com.example.votekt.BuildConfig
import com.example.votekt.data.cache.PrefKeys
import com.example.votekt.data.cache.ProposalsDao
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.transactions.ConfirmTransactionState
import com.example.votekt.domain.transactions.PrepareTransactionData
import com.example.votekt.domain.transactions.SendTransactionRepository
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
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.kethereum.model.Transaction
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.toHexString
import java.math.BigInteger

class SendTransactionRepositoryImpl(
    private val ethereumClient: EthereumClient,
    private val cryptoHelper: CryptoHelper,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val proposalsDao: ProposalsDao,
    private val ksPrefs: KsPrefs,
) : SendTransactionRepository {

    private var transaction: Transaction = createEmptyTransaction()

    private val _state: MutableStateFlow<ConfirmTransactionState> = MutableStateFlow(
        ConfirmTransactionState.Idle
    )
    override val state = _state

    override suspend fun requirePrepareTransaction(data: PrepareTransactionData) = withContext(Dispatchers.IO) {
        val selfAddress = accountRepository.getSelfAddress()

        val recipientAddress = when (data) {
            is PrepareTransactionData.SendValue -> data.receiver
            is PrepareTransactionData.ContractInteraction -> data.contractAddress
        }

        transaction = createEmptyTransaction().apply {
            this.from = selfAddress
            this.to = recipientAddress
            this.chain = BuildConfig.CHAIN_ID.toBigInteger()
        }

        // Initial state
        _state.emit(
            ConfirmTransactionState.TxReview(
                data = data,
                transactionType = when (data) {
                    is PrepareTransactionData.ContractInteraction -> {
                        when (data) {
                            is PrepareTransactionData.ContractInteraction.CreateProposal
                                -> TransactionType.CREATE_PROPOSAL
                            is PrepareTransactionData.ContractInteraction.VoteOnProposal
                                -> TransactionType.VOTE
                        }
                    }

                    is PrepareTransactionData.SendValue -> TransactionType.PAYMENT
                },
                to = recipientAddress,
                value = if (data is PrepareTransactionData.SendValue) data.amount else null,
                input = if (data is PrepareTransactionData.ContractInteraction) data.contractInput.value.toHexString() else null,
                totalEstimatedFee = null,
                minerTipFee = null,
                isSufficientBalance = null,
            )
        )

        when (data) {
            is PrepareTransactionData.ContractInteraction -> {
                transaction.input = data.contractInput.value
                transaction.value = BigInteger.ZERO
            }

            is PrepareTransactionData.SendValue -> {
                transaction.value = data.amount.value
            }
        }

        val transactionEstimation = ethereumClient.estimateTransaction(transaction)
        transaction.applyGasEstimation(transactionEstimation)
    }

    override suspend fun confirmTransaction() = withContext(Dispatchers.IO) {
        val currentState = _state.value
        if (currentState !is ConfirmTransactionState.TxReview) return@withContext

        val credentials = loadCredentials()
        val signedTxData = transaction.signAndEncode(credentials)
        val hash = ethereumClient.sendRawTransaction(signedTxData)

        cacheLocalTransactionData(currentState.data, hash)

        _state.update { ConfirmTransactionState.Idle }
    }

    override suspend fun cancelTransaction() {
        transaction = createEmptyTransaction()
        state.emit(ConfirmTransactionState.Idle)
    }

    private fun Transaction.applyGasEstimation(estimation: EthTransactionEstimation) {
        val currentState = _state.value
        if (currentState !is ConfirmTransactionState.TxReview) return

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

        _state.update {
            currentState.copy(
                totalEstimatedFee = totalFee,
                minerTipFee = maxPriorityFeePerGas,
                isSufficientBalance = estimation.balance > totalFee
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
    )  = withContext(Dispatchers.IO) {
        transactionRepository.addNewTransaction(
            transactionHash = TransactionHash(transactionHash),
            transactionType = prepareTransactionData.transactionType
        )

        when (prepareTransactionData) {
            is PrepareTransactionData.ContractInteraction.CreateProposal -> {
                proposalsDao.updateDeployTransactionHash(
                    proposalId = prepareTransactionData.proposalUuid,
                    newDeployTransactionHash = transactionHash
                )
            }

            is PrepareTransactionData.ContractInteraction.VoteOnProposal -> {
                proposalsDao.updateProposalVote(
                    proposalNumber = prepareTransactionData.proposalNumber,
                    supported = prepareTransactionData.vote,
                    voteTransactionHash = transactionHash,
                )
            }

            else -> {}
        }
    }

    private suspend fun loadCredentials(): ECKeyPair = withContext(Dispatchers.IO) {
        // TODO proper account storing
        Log.d(LOG_TAG, "load credentials for seed: ${ ksPrefs.pull<String>(PrefKeys.ACCOUNT_MNEMONIC_PHRASE)}")
        val phrase = ksPrefs.pull<String>(PrefKeys.ACCOUNT_MNEMONIC_PHRASE)
        val mnemonic = Mnemonics.MnemonicCode(phrase)
        return@withContext cryptoHelper.generateCredentialsFromMnemonic(phrase).ecKeyPair!!
    }

    companion object {
        val DEFAULT_MINER_TIP = 1.5.GWEI
        const val LOG_TAG = "SEND_TX_TAG"
    }
}
