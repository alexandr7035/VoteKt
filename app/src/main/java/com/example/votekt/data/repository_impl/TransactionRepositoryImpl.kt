package com.example.votekt.data.repository_impl

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.transactionFee
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.cache.TransactionEntity
import com.example.votekt.data.workers.AwaitTransactionWorker
import com.example.votekt.data.workers.RefreshProposalsData
import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionHash
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.isContractInteraction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val dispatcher: CoroutineDispatcher,
    private val workManager: WorkManager,
) : TransactionRepository {
    override fun getTransactions(): Flow<List<TransactionDomain>> {
        return transactionDao.getTransactions().map { list ->
            list.map { it.mapToData() }
        }.flowOn(dispatcher)
    }

    override suspend fun getTransactionStatus(transactionHash: TransactionHash): TransactionStatus? {
        // TODO
        return null
    }

    override suspend fun addNewTransaction(
        transactionHash: TransactionHash,
        transactionType: TransactionType,
    ) {
        transactionDao.cacheTransaction(
            TransactionEntity(
                type = transactionType,
                hash = transactionHash.value,
                status = TransactionStatus.PENDING,
                dateSent = System.currentTimeMillis(),
                gasUsed = null,
                gasFee = null,
            )
        )

        val data = Data.Builder()
            .putString(AwaitTransactionWorker.TRANSACTION_HASH, transactionHash.value)
            .build()

        val checkForReceiptWork = OneTimeWorkRequestBuilder<AwaitTransactionWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        val postTransactionWork = OneTimeWorkRequestBuilder<RefreshProposalsData>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        if (transactionType.isContractInteraction()) {
            workManager
                .beginWith(checkForReceiptWork)
                .then(postTransactionWork)
                .enqueue()
        } else {
            workManager
                .enqueue(checkForReceiptWork)
        }
    }

    override suspend fun updateTransaction(receipt: EthTransactionReceipt) {
        val txCached = transactionDao.getTransactionByHash(receipt.transactionHash)
        txCached?.let {
            val txStatus = when {
                receipt.isSuccessful() -> TransactionStatus.MINED
                else -> TransactionStatus.REVERTED
            }

            val updated = txCached.copy(
                status = txStatus,
                gasUsed = receipt.gasUsed,
                gasFee = receipt.transactionFee()
            )
            transactionDao.updateTransaction(updated)
        }
    }

    override suspend fun getTransactionType(transactionHash: TransactionHash): TransactionType? {
        return transactionDao.getTransactionType(transactionHash.value)
    }

    override suspend fun clearTransactions(): OperationResult<Unit> {
        return try {
            transactionDao.clearTransactionHistory()
            OperationResult.Success(Unit)
        } catch (e: Exception) {
            OperationResult.Failure(AppError(ErrorType.UNKNOWN_ERROR))
        }
    }
}