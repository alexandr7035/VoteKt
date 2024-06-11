package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.transactions.TransactionDomain
import by.alexandr7035.votekt.domain.model.transactions.TransactionHash
import by.alexandr7035.votekt.domain.model.transactions.TransactionStatus
import by.alexandr7035.votekt.domain.model.transactions.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeTransactions(): Flow<List<TransactionDomain>>

    suspend fun getTransactionStatus(transactionHash: TransactionHash): TransactionStatus?

    suspend fun addNewTransaction(
        transactionHash: TransactionHash,
        transactionType: TransactionType,
        value: Wei?,
    )

    suspend fun clearTransactions()

    suspend fun updateTransaction(receipt: EthTransactionReceipt)

    suspend fun getTransactionType(
        transactionHash: TransactionHash,
    ): TransactionType?
}
