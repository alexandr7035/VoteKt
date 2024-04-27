package com.example.votekt.domain.usecase.contract

import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.model.contract.CreateDraftProposal
import com.example.votekt.domain.votings.VotingContractRepository

class CreateDraftProposalUseCase(
    private val votingContractRepository: VotingContractRepository
) {
    suspend fun invoke(req: CreateDraftProposal): OperationResult<Uuid> {
        return votingContractRepository.createDraftProposal(req)
    }
}
