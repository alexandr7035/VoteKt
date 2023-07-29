package com.example.votekt.data.impl

import com.example.votekt.data.AppError
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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

    override suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus> = withContext(dispatcher) {
        try {
            // FIXME
            delay(3000)
            // TODO
            val txStatus = TxStatus.CONFIRMED

            if (txStatus != TxStatus.PENDING) {
                val txCached = txDao.getTransactionByHash(txHash)
                if (txCached != null) {
                    val updated = txCached.copy(status = txStatus)
                    txDao.updateTransaction(updated)
                }
            }

            return@withContext OperationResult.Success(txStatus)
        } catch (e: Exception) {
            // TODO
            return@withContext OperationResult.Failure(AppError.UnknownError(e.message ?: ""))
        }
    }
}