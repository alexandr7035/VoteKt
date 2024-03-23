package com.example.votekt.data.repository_impl

import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.ETHER
import by.alexandr7035.ethereum.model.EthTransactionEstimation
import by.alexandr7035.ethereum.model.GWEI
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.BuildConfig
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.transactions.ConfirmTransactionState
import com.example.votekt.domain.transactions.PrepareTransactionData
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.transactions.TransactionType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kethereum.eip1559.signer.signViaEIP1559
import org.kethereum.extensions.transactions.encodeAsEIP1559Tx
import org.kethereum.model.ECKeyPair
import org.kethereum.model.PrivateKey
import org.kethereum.model.PublicKey
import org.kethereum.model.Transaction
import org.kethereum.model.createEmptyTransaction
import org.komputing.khex.extensions.toHexString
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.crypto.Credentials

class SendTransactionRepositoryImpl(
    private val ethereumClient: EthereumClient,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) : SendTransactionRepository {

    private var transaction: Transaction = createEmptyTransaction()

    private val _state: MutableStateFlow<ConfirmTransactionState> = MutableStateFlow(
        ConfirmTransactionState.Idle
    )
    override val state = _state

    // TODO viewmodel handling
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("error repo ${throwable.localizedMessage}")
        throw throwable
    }

    init {
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            requirePrepareTransaction(
                data = PrepareTransactionData.SendValue(
                    amount = 0.25.ETHER,
                    receiver = org.kethereum.model.Address("0x3C44CdDdB6a900fa2b585dd299e03d12FA4293BC")
                )
            )
        }
    }

    override suspend fun requirePrepareTransaction(data: PrepareTransactionData) {
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
                transactionType = when (data) {
                    is PrepareTransactionData.ContractInteraction -> data.operation
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
            }
            is PrepareTransactionData.SendValue -> {
                transaction.value = data.amount.value
            }
        }

        val transactionEstimation = ethereumClient.estimateTransaction(transaction)
        transaction.applyGasEstimation(transactionEstimation)
    }

    override suspend fun confirmTransaction() {
        val currentState = _state.value
        if (currentState !is ConfirmTransactionState.TxReview) return

        // TODO working with credentials
        val credentials = Bip44WalletUtils.loadBip44Credentials("", BuildConfig.TEST_MNEMONIC)

        val signedTxData = transaction.signAndEncode(credentials)
        val hash = ethereumClient.sendRawTransaction(signedTxData)
        transactionRepository.addNewTransaction(
            transactionHash = TransactionHash(hash),
            transactionType = currentState.transactionType
        )

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
            gasLimit = estimation.estimatedGas
            this.maxPriorityFeePerGas = maxPriorityFeePerGas.value
            this.maxFeePerGas = maxFeePerGas.value
            this.nonce = estimation.nonce
        }

        val totalFee = Wei(estimation.estimatedGas * maxFeePerGas.value)

        _state.update {
            currentState.copy(
                totalEstimatedFee = totalFee,
                minerTipFee = maxPriorityFeePerGas,
                isSufficientBalance = estimation.balance > totalFee
            )
        }
    }

    private fun Transaction.signAndEncode(credentials: Credentials): String {
        val signature = this.signViaEIP1559(
            ECKeyPair(
                privateKey = PrivateKey(credentials.ecKeyPair.privateKey),
                publicKey = PublicKey(credentials.ecKeyPair.publicKey)
            )
        )

        return this.encodeAsEIP1559Tx(signature).toHexString()
    }

    companion object {
        val DEFAULT_MINER_TIP = 1.5.GWEI
    }
}
