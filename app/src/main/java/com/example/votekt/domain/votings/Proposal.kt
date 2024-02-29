package com.example.votekt.domain.votings

import by.alexandr7035.ethereum.model.Address
import com.example.votekt.domain.transactions.TransactionHash

sealed class Proposal(
    open val uuid: String,
    open val title: String,
    open val description: String,
    // TODO implement in contract
    open val creatorAddress: Address,
    open val isSelfCreated: Boolean,
) {
   data class Draft(
       override val uuid: String,
       override val title: String,
       override val description: String,
       override val creatorAddress: Address,
       override val isSelfCreated: Boolean,
       val deploymentTransactionHash: TransactionHash?,
       val shouldDeploy: Boolean,
       val deployFailed: Boolean,
   ): Proposal(uuid, title, description, creatorAddress, isSelfCreated)

    data class Deployed(
        override val uuid: String,
        override val title: String,
        override val description: String,
        override val creatorAddress: Address,
        override val isSelfCreated: Boolean,
        val proposalNumber: Long,
        val expirationTime: Long,
        private val votesFor: Int,
        private val votesAgainst: Int,
        val selfVote: VoteType? = null,
        val selfVoteTransaction: TransactionHash? = null,
    ): Proposal(uuid, title, description, creatorAddress, isSelfCreated) {
        val isFinished
            get() = this.expirationTime < System.currentTimeMillis()

        // TODO refactoring
        val votingData
            get() = VotingData(
                votesFor = votesFor,
                votesAgainst = votesAgainst,
                selfVote = selfVote
            )
    }
}
