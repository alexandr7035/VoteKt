package com.example.votekt.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.votekt.domain.votings.VotingRepository

class SyncProposalsWorker(
    appContext: Context,
    params: WorkerParameters,
    private val votingRepository: VotingRepository,
): CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            votingRepository.syncProposalsWithContract()
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "failed to sync proposals: ${e.localizedMessage}")
            Result.failure()
        }
    }

    companion object {
        private val TAG = SyncProposalsWorker::class.simpleName
    }
}