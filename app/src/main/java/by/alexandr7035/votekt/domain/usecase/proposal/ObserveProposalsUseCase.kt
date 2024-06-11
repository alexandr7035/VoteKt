package by.alexandr7035.votekt.domain.usecase.proposal

import by.alexandr7035.votekt.domain.model.proposal.Proposal
import by.alexandr7035.votekt.domain.repository.VotingContractRepository
import kotlinx.coroutines.flow.Flow

class ObserveProposalsUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(): Flow<List<Proposal>> {
        return votingContractRepository.observeProposals()
    }
}
