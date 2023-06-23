package com.example.votekt.data

import com.example.votekt.data.model.Proposal

interface Web3Repository {
    // TODO
    suspend fun getVotedAddresses(): List<VoterAddress>
    suspend fun getProposalById(id: Long): OperationResult<Proposal>
    suspend fun getProposals(): OperationResult<List<Proposal>>
}

