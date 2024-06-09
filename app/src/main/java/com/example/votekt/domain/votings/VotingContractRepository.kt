package com.example.votekt.domain.votings

import by.alexandr7035.ethereum.model.events.EthereumEvent
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.model.contract.ContractConfiguration
import com.example.votekt.domain.model.contract.ContractState
import com.example.votekt.domain.model.contract.CreateDraftProposal
import kotlinx.coroutines.flow.Flow

interface VotingContractRepository {
    fun observeContractState(): Flow<ContractState>
    fun getContractConfiguration(): ContractConfiguration
    fun observeProposals(): Flow<List<Proposal>>
    fun observeProposalById(id: String): Flow<Proposal>
    suspend fun createDraftProposal(req: CreateDraftProposal): OperationResult<Uuid>
    suspend fun deployDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    suspend fun deleteDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<Unit>
    suspend fun syncProposalsWithContract()
    suspend fun handleContractEvent(event: EthereumEvent.ContractEvent)
    suspend fun clearContractData()
}
