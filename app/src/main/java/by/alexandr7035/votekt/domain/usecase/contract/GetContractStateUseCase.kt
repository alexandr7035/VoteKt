package by.alexandr7035.votekt.domain.usecase.contract

import by.alexandr7035.votekt.domain.model.contract.ContractState
import by.alexandr7035.votekt.domain.repository.VotingContractRepository
import kotlinx.coroutines.flow.Flow

class GetContractStateUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(): Flow<ContractState> {
        return votingContractRepository.observeContractState()
    }
}
