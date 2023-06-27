package com.example.votekt.data

import com.example.votekt.data.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>

    suspend fun cacheTransaction(transaction: Transaction)
}