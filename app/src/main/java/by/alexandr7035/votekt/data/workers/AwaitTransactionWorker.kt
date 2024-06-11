package by.alexandr7035.votekt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.errors.TransactionReceiptNotFound
import by.alexandr7035.votekt.domain.repository.TransactionRepository

class AwaitTransactionWorker(
    appContext: Context,
    params: WorkerParameters,
    private val transactionRepository: TransactionRepository,
    private val ethereumClient: EthereumClient
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val transactionHash = inputData.getString(TRANSACTION_HASH)
            ?: return Result.failure()

        Log.d(TAG, "Start for $transactionHash")

        return try {
            val receipt = ethereumClient.getTransactionReceipt(transactionHash)
            transactionRepository.updateTransaction(receipt)
            Log.d(TAG, "Completed for $transactionHash")
            Result.success()
        } catch (e: TransactionReceiptNotFound) {
            Log.d(TAG, "Receipt not found for $transactionHash, retry")
            Result.retry()
        } catch (e: Exception) {
            Log.e(TAG, "failed to update $transactionHash: ${e.localizedMessage}")
            Result.failure()
        }
    }

    companion object {
        private val TAG = "${AwaitTransactionWorker::class.simpleName}"
        const val TRANSACTION_HASH = "transactionHash"
        const val INITIAL_DELAY = 5L
        const val BACKOFF_DELAY_SEC = 10L
    }
}
