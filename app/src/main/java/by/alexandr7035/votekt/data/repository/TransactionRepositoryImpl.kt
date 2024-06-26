package by.alexandr7035.votekt.data.repository

import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import by.alexandr7035.ethereum.model.EthTransactionReceipt
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.transactionFee
import by.alexandr7035.votekt.data.cache.TransactionDao
import by.alexandr7035.votekt.data.cache.TransactionEntity
import by.alexandr7035.votekt.data.workers.AwaitTransactionWorker
import by.alexandr7035.votekt.data.workers.RefreshSelfBalanceWorker
import by.alexandr7035.votekt.data.workers.SyncProposalsWorker
import by.alexandr7035.votekt.domain.model.transactions.TransactionDomain
import by.alexandr7035.votekt.domain.model.transactions.TransactionHash
import by.alexandr7035.votekt.domain.model.transactions.TransactionStatus
import by.alexandr7035.votekt.domain.model.transactions.TransactionType
import by.alexandr7035.votekt.domain.model.transactions.isContractInteraction
import by.alexandr7035.votekt.domain.repository.TransactionRepository
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
    override fun observeTransactions(): Flow<List<TransactionDomain>> {
        return transactionDao.getTransactions().map { list ->
            list.map { it.mapToData() }
        }.flowOn(dispatcher)
    }

    override suspend fun addNewTransaction(
        transactionHash: TransactionHash,
        transactionType: TransactionType,
        value: Wei?,
    ) {
        transactionDao.cacheTransaction(
            TransactionEntity(
                type = transactionType,
                hash = transactionHash.value,
                status = TransactionStatus.PENDING,
                dateSent = System.currentTimeMillis(),
                gasUsed = null,
                gasFee = null,
                value = value,
            )
        )

        val checkForReceiptWork = OneTimeWorkRequestBuilder<AwaitTransactionWorker>()
            .setConstraints(AwaitTransactionWorker.CONSTRAINTS)
            .setInitialDelay(
                duration = AwaitTransactionWorker.INITIAL_DELAY,
                timeUnit = TimeUnit.SECONDS
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = AwaitTransactionWorker.BACKOFF_DELAY_SEC,
                timeUnit = TimeUnit.SECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(AwaitTransactionWorker.TRANSACTION_HASH, transactionHash.value)
                    .build()
            )
            .build()

        val postTransactionWork = OneTimeWorkRequestBuilder<SyncProposalsWorker>()
            .setConstraints(SyncProposalsWorker.CONSTRAINTS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                backoffDelay = SyncProposalsWorker.BACKOFF_DELAY_SEC,
                timeUnit = TimeUnit.SECONDS
            )
            .build()

        val refreshSelfBalanceWork = OneTimeWorkRequestBuilder<RefreshSelfBalanceWorker>()
            .setConstraints(RefreshSelfBalanceWorker.CONSTRAINTS)
            .build()

        if (transactionType.isContractInteraction()) {
            workManager
                .beginWith(checkForReceiptWork)
                .then(postTransactionWork)
                .then(refreshSelfBalanceWork)
                .enqueue()
        } else {
            workManager
                .beginWith(checkForReceiptWork)
                .then(refreshSelfBalanceWork)
                .enqueue()
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

    override suspend fun clearTransactions() {
        transactionDao.clearTransactionHistory()
    }
}
