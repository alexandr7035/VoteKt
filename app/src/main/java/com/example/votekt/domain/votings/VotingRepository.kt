package com.example.votekt.domain.votings

import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.transactions.TransactionHash

interface VotingRepository {
    suspend fun getProposalById(id: Long): OperationResult<Proposal>
    suspend fun getProposals(): OperationResult<List<Proposal>>
    suspend fun createProposal(req: CreateProposal): OperationResult<String>
    suspend fun voteOnProposal(proposalId: Long, vote: VoteType): OperationResult<TransactionHash>
}

