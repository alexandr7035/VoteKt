package com.example.votekt.data.impl

import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TransactionRepositoryImpl(
    private val txDao: TransactionDao,
    private val dispatcher: CoroutineDispatcher
) : TransactionRepository {
    override suspend fun getTransactions(): List<Transaction> = withContext(dispatcher) {
        return@withContext txDao.getTransactions().map {
            Transaction(it.hash, it.status, it.dateSent)
        }
    }

    override suspend fun cacheTransaction(transaction: Transaction) {
        txDao.cacheTransaction(
            TransactionEntity(
                hash = transaction.hash,
                status = transaction.status,
                dateSent = transaction.dateSent
            )
        )
    }
}