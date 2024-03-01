package com.example.votekt.domain.votings

data class VotingData(
    val votesFor: Int,
    val votesAgainst: Int,
    val selfVote: VoteType?,
) {
    private val totalVotes: Int
        get() = votesFor + votesAgainst
    val votesForPercentage: Float = votesFor.toFloat() / totalVotes
    val votesAgainstPercentage: Float = votesAgainst.toFloat() / totalVotes

    fun hasVotes() = totalVotes > 0
}
