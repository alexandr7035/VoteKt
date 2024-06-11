package by.alexandr7035.votekt.domain.usecase.contract

import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.core.Uuid
import by.alexandr7035.votekt.domain.model.contract.CreateDraftProposal
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class CreateDraftProposalUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke(req: CreateDraftProposal): OperationResult<Uuid> {
        return votingContractRepository.createDraftProposal(req)
    }
}
