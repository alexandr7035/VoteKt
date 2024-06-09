package com.example.votekt.domain.datasync

import com.example.votekt.domain.votings.VotingContractRepository

class SyncWithContractUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke() {
        votingContractRepository.syncProposalsWithContract()
    }
}
