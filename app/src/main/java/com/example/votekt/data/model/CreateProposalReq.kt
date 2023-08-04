package com.example.votekt.data.model

data class CreateProposalReq(
    val title: String,
    val desc: String,
    val duration: ProposalDuration
)
