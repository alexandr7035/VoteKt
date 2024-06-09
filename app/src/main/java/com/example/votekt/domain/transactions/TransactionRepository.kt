package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.core.OperationResult
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<List<TransactionDomain>>

    suspend fun getTransactionStatus(transactionHash: TransactionHash): TransactionStatus?

    suspend fun addNewTransaction(
        transactionHash: TransactionHash,
        transactionType: TransactionType,
        value: Wei?,
    )

    suspend fun clearTransactions(): OperationResult<Unit>

    suspend fun updateTransaction(receipt: EthTransactionReceipt)

    suspend fun getTransactionType(
        transactionHash: TransactionHash,
    ): TransactionType?
}
