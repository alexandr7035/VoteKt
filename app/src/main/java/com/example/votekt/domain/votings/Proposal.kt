package com.example.votekt.domain.votings

data class Proposal(
    val id: Long,
    val title: String,
    val description: String,
    val votingData: VotingData,
    val expirationTime: Long
) {
    companion object {}
}