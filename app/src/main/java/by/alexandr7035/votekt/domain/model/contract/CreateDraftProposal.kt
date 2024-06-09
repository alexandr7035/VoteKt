package by.alexandr7035.votekt.domain.model.contract

import kotlin.time.Duration

data class CreateDraftProposal(
    val title: String,
    val desc: String,
    val duration: Duration,
)
