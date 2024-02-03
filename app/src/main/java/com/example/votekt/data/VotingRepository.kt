package com.example.votekt.data

import com.example.votekt.data.model.CreateProposalReq
import com.example.votekt.data.model.Proposal

interface VotingRepository {
    // TODO
    suspend fun getVotedAddresses(): List<VoterAddress>
    suspend fun getProposalById(id: Long): OperationResult<Proposal>
    suspend fun getProposals(): OperationResult<List<Proposal>>
    suspend fun createProposal(req: CreateProposalReq): OperationResult<String>
    suspend fun voteOnProposal(proposalId: Long, isFor: Boolean): OperationResult<String>
}

