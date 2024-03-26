package com.example.votekt.domain.votings

import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash
import kotlinx.coroutines.flow.Flow

// TODO redeploy proposal
// TODO delete draft proposal
interface VotingRepository {
    fun getProposals(): Flow<List<Proposal>>
    fun getProposalById(id: String): Flow<Proposal>

    // TODO refactoring of OperationResult class
    suspend fun createProposal(req: CreateProposal): OperationResult<Unit>
    suspend fun voteOnProposal(proposalNumber: Int, vote: VoteType): OperationResult<Unit>
    suspend fun syncProposalsWithContract()
}

