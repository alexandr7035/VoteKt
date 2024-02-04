package com.example.votekt.domain.votings

data class VotingData(
    val votesFor: Int,
    val votesAgainst: Int,
) {
    val total: Int = votesFor + votesAgainst
    val votesForPercentage: Float = votesFor.toFloat() / total
    val votesAgainstPercentage: Float = votesAgainst.toFloat() / total

    fun hasVotes() = total > 0
}
