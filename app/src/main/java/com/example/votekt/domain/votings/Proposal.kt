package com.example.votekt.domain.votings

import org.kethereum.model.Address
import com.example.votekt.domain.core.BlockchainActionStatus
import com.example.votekt.domain.transactions.TransactionDomain
import kotlin.time.Duration

sealed class Proposal(
    open val uuid: String,
    open val title: String,
    open val description: String,
    open val creatorAddress: Address,
    open val isSelfCreated: Boolean = false,
) {
    fun hasVotes() = this is Deployed && this.votingData.hasVotes()

   data class Draft(
       override val uuid: String,
       override val title: String,
       override val description: String,
       override val creatorAddress: Address,
       override val isSelfCreated: Boolean,
       val deploymentTransaction: TransactionDomain?,
       val deployStatus: BlockchainActionStatus,
       val duration: Duration,
   ): Proposal(uuid, title, description, creatorAddress, isSelfCreated)

    data class Deployed(
        override val uuid: String,
        override val title: String,
        override val description: String,
        override val creatorAddress: Address,
        override val isSelfCreated: Boolean,
        val proposalNumber: Int,
        val expirationTime: Long,
        val votingData: VotingData,
        val voteTransaction: TransactionDomain?,
        val selfVoteStatus: BlockchainActionStatus,
    ): Proposal(uuid, title, description, creatorAddress, isSelfCreated) {
        val isFinished
            get() = this.expirationTime < System.currentTimeMillis()
    }
}
