package com.example.votekt.data.local

import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.transactionFee
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionDataSource(
    private val transactionsDao: TransactionDao
) {
    fun getTransactions(): Flow<List<Transaction>> {
        return transactionsDao.getTransactions().map { list ->
            list.map { it.mapToData() }
        }
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
        receipt: EthTransactionReceipt
    ) {
        val txCached = transactionsDao.getTransactionByHash(receipt.transactionHash)
        txCached?.let {
            val txStatus = when {
                receipt.isSuccessful() -> TxStatus.MINED
                else -> TxStatus.REVERTED
            }

            val updated = txCached.copy(
                status = txStatus,
                gasUsed = receipt.gasUsed,
                gasFee = receipt.transactionFee()
            )
            transactionsDao.updateTransaction(updated)
        }
    }

    suspend fun clearLocalTransactionHistory() {
        transactionsDao.clearTransactionHistory()
    }
}