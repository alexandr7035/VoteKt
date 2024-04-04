package com.example.votekt.domain.votings

import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.core.Uuid
import kotlinx.coroutines.flow.Flow

// TODO redeploy proposal
// TODO delete draft proposal
interface VotingContractRepository {
    fun getProposals(): Flow<List<Proposal>>
    fun getProposalById(id: String): Flow<Proposal>

    // TODO refactoring of OperationResult class
    suspend fun createProposal(req: CreateProposal): OperationResult<Uuid>

    suspend fun deployDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    // TODO move to local datasource as not a contract feature
    suspend fun deleteDraftProposal(proposalUuid: Uuid): OperationResult<Unit>
    suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<Unit>
    suspend fun syncProposalsWithContract()
    suspend fun handleContractEvent(event: EthereumEvent.ContractEvent)
}

