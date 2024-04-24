package com.example.votekt.domain.usecase.contract

import com.example.votekt.domain.model.contract.ContractState
import com.example.votekt.domain.votings.VotingContractRepository
import kotlinx.coroutines.flow.Flow

class GetContractStateUseCase(
    private val votingContractRepository: VotingContractRepository,
) {
    fun invoke(): Flow<ContractState> {
        return votingContractRepository.getContractState()
    }
}