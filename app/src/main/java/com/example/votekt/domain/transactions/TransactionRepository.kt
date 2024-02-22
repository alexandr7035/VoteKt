package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.EthTransactionReceipt
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getTransactions(): Flow<List<TransactionDomain>>

    suspend fun addNewTransaction(
        transactionHash: TransactionHash,
        type: TransactionType,
    )

    suspend fun clearTransactions(): OperationResult<Unit>

    suspend fun updateTransaction(receipt: EthTransactionReceipt)
}