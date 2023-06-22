package com.example.votekt.data.impl

import com.example.votekt.data.VoterAddress
import com.example.votekt.data.Web3Repository
import kotlinx.coroutines.delay
import org.web3j.protocol.Web3j

class Web3RepositoryImpl(web3j: Web3j) : Web3Repository {
    override suspend fun getVotedAddresses(): List<VoterAddress> {
        delay(3000)

        val addresses = List(12) { index ->
            VoterAddress(address = "0x32424535esdf3242344bc${index}", votedFor = index % 2 == 0)
        }

        return addresses
    }
}