package by.alexandr7035.votekt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.WorkerParameters
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.repository.AccountRepository

class RefreshSelfBalanceWorker(
    appContext: Context,
    params: WorkerParameters,
    private val accountRepository: AccountRepository
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val result = OperationResult.runWrapped {
            accountRepository.refreshBalance()
        }
        return when (result) {
            is OperationResult.Failure -> {
                Log.d(TAG, "balance sync error ${result.error.message}")
                Result.failure()
            }

            is OperationResult.Success -> {
                Log.d(TAG, "balance sync success")
                Result.success()
            }
        }
    }

    companion object {
        val TAG = this::class.java.simpleName

        val CONSTRAINTS = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()
    }
}
