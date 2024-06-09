package by.alexandr7035.votekt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import by.alexandr7035.votekt.domain.votings.VotingContractRepository

class SyncProposalsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val votingContractRepository: VotingContractRepository,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            votingContractRepository.syncProposalsWithContract()
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "failed to sync proposals: ${e.localizedMessage}")
            Result.failure()
        }
    }

    companion object {
        private val TAG = SyncProposalsWorker::class.simpleName
        const val BACKOFF_DELAY_SEC = 10L
    }
}
