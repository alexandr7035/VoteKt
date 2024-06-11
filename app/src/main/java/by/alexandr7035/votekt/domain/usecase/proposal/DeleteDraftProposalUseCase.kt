package by.alexandr7035.votekt.domain.usecase.proposal

import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.core.Uuid
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class DeleteDraftProposalUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    suspend fun invoke(proposalUuid: Uuid): OperationResult<Unit> {
        return votingContractRepository.deleteDraftProposal(proposalUuid)
    }
}
