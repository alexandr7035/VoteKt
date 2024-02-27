package com.example.votekt.domain.votings

import com.example.votekt.domain.transactions.TransactionHash

sealed class Proposal(
    open val id: Int,
    open val title: String,
    open val description: String,
) {
   data class Draft(
       override val id: Int,
       override val title: String,
       override val description: String,
       val deploymentTransactionHash: TransactionHash?,
   ): Proposal(id, title, description)

    data class Deployed(
        override val id: Int,
        override val title: String,
        override val description: String,
        val blockchainId: Long,
        val expirationTime: Long,
        private val votesFor: Int,
        private val votesAgainst: Int,
        val selfVote: VoteType? = null,
        val selfVoteTransaction: TransactionHash? = null,
    ): Proposal(id, title, description) {
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
