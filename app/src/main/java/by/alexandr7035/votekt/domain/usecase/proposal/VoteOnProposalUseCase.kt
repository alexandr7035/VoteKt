package by.alexandr7035.votekt.domain.usecase.proposal

import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.model.proposal.VoteType
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class VoteOnProposalUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke(
        proposalNumber: Int,
        vote: VoteType
    ): OperationResult<Unit> {
        return votingContractRepository.voteOnProposal(proposalNumber, vote)
    }
}
