package com.example.votekt.data.local

import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.data.web3_core.transactions.TxHash

class TransactionDataSource(
    private val transactionsDao: TransactionDao
) {
    suspend fun getTransactions(): List<Transaction> {
        return transactionsDao.getTransactions().map { it.mapToData()}
    }

    suspend fun cacheTransaction(transaction: Transaction) {
        transactionsDao.cacheTransaction(
            TransactionEntity(
                type = transaction.type,
                hash = transaction.hash,
                status = transaction.status,
                dateSent = transaction.dateSent,
            )
        )
    }

    suspend fun updateCachedTransactionStatus(
        txHash: TxHash,
        newStatus: TxStatus
    ) {
        val txCached = transactionsDao.getTransactionByHash(txHash.value)
        if (txCached != null) {
            val updated = txCached.copy(status = newStatus)
            transactionsDao.updateTransaction(updated)
        }
    }

    suspend fun clearLocalTransactionHistory() {
        transactionsDao.clearTransactionHistory()
    }
}