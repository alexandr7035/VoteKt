package com.example.votekt.data.impl

import com.example.votekt.data.AppError
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.local.TransactionDataSource
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.data.web3_core.transactions.TxStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.web3j.protocol.Web3j

class TransactionRepositoryImpl(
    private val transactionDataSource: TransactionDataSource,
    private val web3j: Web3j,
    private val dispatcher: CoroutineDispatcher,
) : TransactionRepository {
    override suspend fun getTransactions(): List<Transaction> = withContext(dispatcher) {
        return@withContext transactionDataSource.getTransactions()
    }

    override suspend fun cacheTransaction(transaction: Transaction) {
        transactionDataSource.cacheTransaction(transaction)
    }

    override suspend fun clearTransactions(): OperationResult<Unit> {
        return try {
            transactionDataSource.clearLocalTransactionHistory()
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Failure(AppError.UnknownError(e.toString()))
        }
    }

    override suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus> = withContext(dispatcher) {
        try {
            val receipt = web3j.ethGetTransactionReceipt(txHash).send().result

            val txStatus = when {
                receipt == null -> TxStatus.PENDING
                receipt.isStatusOK && receipt.revertReason == null -> TxStatus.MINED
                else -> TxStatus.REVERTED
            }

            if (txStatus != TxStatus.PENDING) {
                transactionDataSource.updateCachedTransactionStatus(
                    txHash = TxHash(txHash),
                    newStatus = txStatus
                )
            }

            return@withContext OperationResult.Success(txStatus)
        }
        catch (e: Exception) {
            return@withContext OperationResult.Failure(AppError.UnknownError(e.message ?: ""))
        }
    }
}