package com.example.votekt.data

import com.example.votekt.data.model.Proposal
import com.example.votekt.data.model.TxStatus

interface Web3Repository {
    // TODO
    suspend fun getVotedAddresses(): List<VoterAddress>
    suspend fun getProposalById(id: Long): OperationResult<Proposal>
    suspend fun getProposals(): OperationResult<List<Proposal>>
    suspend fun createProposal(title: String, description: String)
}

