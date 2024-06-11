package by.alexandr7035.votekt.domain.usecase.proposal

import by.alexandr7035.votekt.domain.model.proposal.Proposal
import by.alexandr7035.votekt.domain.repository.VotingContractRepository
import kotlinx.coroutines.flow.Flow

class ObserveProposalUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(proposalId: String): Flow<Proposal> {
        return votingContractRepository.observeProposalById(proposalId)
    }
}
