package com.example.votekt.domain.votings

data class CreateProposal(
    val title: String,
    val desc: String,
    val duration: ProposalDuration
)
