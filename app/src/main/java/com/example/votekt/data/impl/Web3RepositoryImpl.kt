package com.example.votekt.data.impl

import com.example.votekt.data.VoterAddress
import com.example.votekt.data.Web3Repository
import kotlinx.coroutines.delay

// TODO DI
class Web3RepositoryImpl() : Web3Repository {
    override suspend fun getVotedAddresses(): List<VoterAddress> {
        delay(3000)

        val addresses = List(12) { index ->
            VoterAddress(address = "0x32424535esdf3242344bc${index}", votedFor = index % 2 == 0)
        }

        return addresses
    }
}