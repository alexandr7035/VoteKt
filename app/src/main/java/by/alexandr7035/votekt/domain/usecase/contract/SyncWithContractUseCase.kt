package by.alexandr7035.votekt.domain.usecase.contract

import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class SyncWithContractUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke() {
        votingContractRepository.syncProposalsWithContract()
    }
}
