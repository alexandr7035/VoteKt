package com.example.votekt.data.impl

import android.util.Log
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.errors.TransactionReceiptNotFound
import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.local.TransactionDataSource
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.domain.core.ErrorType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TransactionRepositoryImpl(
    private val transactionDataSource: TransactionDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val ethereumClient: EthereumClient,
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
            OperationResult.Failure(AppError(ErrorType.UNKNOWN_ERROR))
        }
    }

    override suspend fun refreshTxStatus(txHash: String): OperationResult<TxStatus> = withContext(dispatcher) {
        try {
            val receipt = try {
                ethereumClient.getTransactionReceipt(
                    transactionHash = txHash
                )
            } catch (e: TransactionReceiptNotFound) {
                null
            }

            Log.d("WEB3_TAG", "receipt $receipt")

            val txStatus = when {
                receipt == null -> TxStatus.PENDING
                receipt.isSuccessful() -> TxStatus.MINED
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
            throw e
            return@withContext OperationResult.Failure(AppError(ErrorType.UNKNOWN_ERROR))
        }
    }
}