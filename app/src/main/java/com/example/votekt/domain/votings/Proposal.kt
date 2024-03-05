package com.example.votekt.domain.votings

import by.alexandr7035.ethereum.model.Address
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionHash

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
       val shouldBeDeployed: Boolean,
       val isDeployFailed: Boolean,
       val isDeployPending: Boolean,
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
        val selfVote: VoteType? = null,
        val selfVoteTransaction: TransactionHash? = null,
    ): Proposal(uuid, title, description, creatorAddress, isSelfCreated) {
        val isFinished
            get() = this.expirationTime < System.currentTimeMillis()
    }
}
