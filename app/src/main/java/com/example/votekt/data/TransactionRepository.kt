package com.example.votekt.data

import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>

    suspend fun cacheTransaction(transaction: Transaction)

    suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus>
}