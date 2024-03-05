package com.example.votekt.domain.votings

data class VotingData(
    val votesFor: Int,
    val votesAgainst: Int,
    val selfVote: VoteType?,
) {
    private val totalVotes: Int
        get() = votesFor + votesAgainst

    val votesForPercentage: Float
        get() = votesFor.toFloat() / totalVotes
    val votesAgainstPercentage: Float
        get() = votesAgainst.toFloat() / totalVotes

    val isSupported: Boolean
        get() = votesForPercentage > votesAgainstPercentage

    fun hasVotes() = totalVotes > 0
}
