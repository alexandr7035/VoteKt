package com.example.votekt.domain.usecase.contract

import com.example.votekt.domain.model.contract.ContractConfiguration
import com.example.votekt.domain.votings.VotingContractRepository

class GetContractConfigurationUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(): ContractConfiguration {
        return votingContractRepository.getContractConfiguration()
    }
}
