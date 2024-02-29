package com.example.votekt.domain.votings

import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash
import kotlinx.coroutines.flow.Flow

interface VotingRepository {
    fun getProposals(): Flow<List<Proposal>>
    fun getProposalById(id: String): Flow<Proposal>
    suspend fun createProposal(req: CreateProposal): OperationResult<String>
    suspend fun voteOnProposal(proposalId: Long, vote: VoteType): OperationResult<TransactionHash>
    suspend fun syncProposalsWithContract()
}

