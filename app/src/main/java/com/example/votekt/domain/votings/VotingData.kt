package com.example.votekt.domain.votings

data class VotingData(
    val votesFor: Int,
    val votesAgainst: Int,
    val selfVote: VoteType?,
) {
    private val totalVotes: Int
        get() = votesFor + votesAgainst

    val votesForPercentage: Float
        get() = if (totalVotes > 0) {
            votesFor.toFloat() / totalVotes
        } else {
            0f
        }

    val votesAgainstPercentage: Float
        get() = if (totalVotes > 0) {
            votesAgainst.toFloat() / totalVotes
        } else {
            0f
        }

    val isSupported: Boolean
        get() = votesForPercentage > votesAgainstPercentage

    fun hasVotes() = totalVotes > 0
}
