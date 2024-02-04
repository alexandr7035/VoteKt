package com.example.votekt.data

import com.example.votekt.data.model.CreateProposalReq
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.votings.VoteType

interface VotingRepository {
    // TODO
    suspend fun getVotedAddresses(): List<VoterAddress>
    suspend fun getProposalById(id: Long): OperationResult<Proposal>
    suspend fun getProposals(): OperationResult<List<Proposal>>
    suspend fun createProposal(req: CreateProposalReq): OperationResult<String>
    suspend fun voteOnProposal(proposalId: Long, vote: VoteType): OperationResult<String>
}

