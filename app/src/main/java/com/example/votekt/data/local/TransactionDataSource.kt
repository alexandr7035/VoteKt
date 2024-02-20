package com.example.votekt.data.local

import by.alexandr7035.ethereum.model.TransactionReceipt
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
                gasUsed = null,
                gasFee = null,
            )
        )
    }

    suspend fun updateCachedTransactionStatus(
        receipt: TransactionReceipt
    ) {
        val txCached = transactionsDao.getTransactionByHash(receipt.transactionHash)
        if (txCached != null) {

            val txStatus = when {
                receipt.isSuccessful() -> TxStatus.MINED
                else -> TxStatus.REVERTED
            }

            val updated = txCached.copy(
                status = txStatus,
                gasUsed = receipt.gasUsed,
                gasFee = Wei(receipt.effectiveGasPrice * receipt.gasUsed)
            )
            transactionsDao.updateTransaction(updated)
        }
    }

    suspend fun clearLocalTransactionHistory() {
        transactionsDao.clearTransactionHistory()
    }
}