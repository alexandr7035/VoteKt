package by.alexandr7035.votekt.domain.usecase.contract

import by.alexandr7035.votekt.domain.model.contract.ContractConfiguration
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class GetContractConfigurationUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(): ContractConfiguration {
        return votingContractRepository.getContractConfiguration()
    }
}
