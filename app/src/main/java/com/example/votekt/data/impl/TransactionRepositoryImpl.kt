package com.example.votekt.data.impl

import android.util.Log
import com.example.votekt.data.AppError
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j
import org.web3j.utils.RevertReasonExtractor

class TransactionRepositoryImpl(
    private val txDao: TransactionDao,
    private val web3j: Web3j,
    private val dispatcher: CoroutineDispatcher,
) : TransactionRepository {
    override suspend fun getTransactions(): List<Transaction> = withContext(dispatcher) {
        return@withContext txDao.getTransactions().map {
            Transaction(
                it.type,
                it.hash,
                it.status,
                it.dateSent,
            )
        }
    }

    override suspend fun cacheTransaction(transaction: Transaction) {
        txDao.cacheTransaction(
            TransactionEntity(
                type = transaction.type,
                hash = transaction.hash,
                status = transaction.status,
                dateSent = transaction.dateSent,
            )
        )
    }

    override suspend fun clearTransactions(): OperationResult<Unit> {
        return try {
            txDao.clearTransactionHistory()
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Failure(AppError.UnknownError(e.toString()))
        }
    }

    override suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus> = withContext(dispatcher) {
        try {
            val receipt = web3j.ethGetTransactionReceipt(txHash).send().result
            val reason = RevertReasonExtractor.extractRevertReason(receipt, null, web3j, true)
            Log.d("TX_TAG", "revert reason $reason")

            val txStatus = when {
                receipt == null -> TxStatus.PENDING
                receipt.isStatusOK && receipt.revertReason == null -> TxStatus.MINED
                else -> TxStatus.REVERTED
            }

            if (txStatus != TxStatus.PENDING) {
                val txCached = txDao.getTransactionByHash(txHash)
                if (txCached != null) {
                    val updated = txCached.copy(status = txStatus)
                    txDao.updateTransaction(updated)
                }
            }

            return@withContext OperationResult.Success(txStatus)
        }
        catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.UnknownError(e.message ?: ""))
        }
    }
}