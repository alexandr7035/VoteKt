package com.example.votekt.data

interface Web3Repository {
    suspend fun getVotedAddresses(): List<VoterAddress>
}

