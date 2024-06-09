package by.alexandr7035.votekt.domain.datasync

import by.alexandr7035.votekt.domain.votings.VotingContractRepository

class SyncWithContractUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke() {
        votingContractRepository.syncProposalsWithContract()
    }
}
