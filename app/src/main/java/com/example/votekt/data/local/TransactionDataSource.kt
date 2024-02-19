package com.example.votekt.data.local

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.data.web3_core.transactions.TxHash
import java.math.BigInteger

class TransactionDataSource(
    private val transactionsDao: TransactionDao
) {
    suspend fun getTransactions(): List<Transaction> {
        return transactionsDao.getTransactions().map { it.mapToData() }
    }

    suspend fun cacheTransaction(transaction: Transaction) {
        transactionsDao.cacheTransaction(
            TransactionEntity(
                type = transaction.type,
                hash = transaction.hash,
                status = transaction.status,
                dateSent = transaction.dateSent,
                gasUsed = null
            )
        )
    }

    suspend fun updateCachedTransactionStatus(
        txHash: TxHash,
        newStatus: TxStatus,
        gasUsed: BigInteger?
    ) {
        val txCached = transactionsDao.getTransactionByHash(txHash.value)
        if (txCached != null) {
            val updated = txCached.copy(
                status = newStatus,
                gasUsed = gasUsed
            )
            transactionsDao.updateTransaction(updated)
        }
    }

    suspend fun clearLocalTransactionHistory() {
        transactionsDao.clearTransactionHistory()
    }
}