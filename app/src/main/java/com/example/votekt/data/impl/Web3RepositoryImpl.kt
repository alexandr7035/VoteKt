package com.example.votekt.data.impl

import android.util.Log
import com.example.votekt.BuildConfig
import com.example.votekt.contracts.VotingContract
import com.example.votekt.data.VoterAddress
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.helpers.executeWeb3Call
import com.example.votekt.data.model.Proposal
import kotlinx.coroutines.delay
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class Web3RepositoryImpl(web3j: Web3j) : Web3Repository {
    private val votingContract: VotingContract

    init {
        val credentials = Bip44WalletUtils.loadBip44Credentials("", BuildConfig.TEST_MNEMONIC)
        Log.d("DEBUG_TAG", "KEY PAIR for ${credentials.address} created")

        val txManager = RawTransactionManager(web3j, credentials)

        // TODO gas estimation
        votingContract = VotingContract.load(
            BuildConfig.CONTRACT_ADDRESS, web3j, txManager, DefaultGasProvider()
        )
    }

    override suspend fun getProposalById(id: Long): Result<Proposal> {
        return executeWeb3Call {
            val res = votingContract.getProposalDetails(BigInteger.valueOf(id)).send()
            Proposal(
                // TODO
                id = "0",
                title = res.title,
                description = res.description,
                votesAgainst = res.votesAgainst.toInt(),
                votesFor = res.votesFor.toInt(),
                expirationTime = res.expirationTime.toLong()
            )
        }
    }

    override suspend fun getProposals(): Result<List<Proposal>> {
        return executeWeb3Call {
            val raw = votingContract.proposalsList.send()
            raw.map { it as VotingContract.ProposalRaw }.map { rawProposal ->
                Proposal(
                    // TODO
                    id = "0",
                    title = rawProposal.title,
                    description = rawProposal.description,
                    votesAgainst = rawProposal.votesAgainst.toInt(),
                    votesFor = rawProposal.votesFor.toInt(),
                    expirationTime = rawProposal.expirationTime.toLong()
                )
            }
        }
    }

    override suspend fun getVotedAddresses(): List<VoterAddress> {
        delay(3000)

        val addresses = List(12) { index ->
            VoterAddress(address = "0x32424535esdf3242344bc${index}", votedFor = index % 2 == 0)
        }

        return addresses
    }
}