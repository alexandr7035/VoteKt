package com.example.votekt.ui.feature_create_proposal.model

import kotlin.time.Duration

sealed class CreateProposalScreenIntent {
    object EnterScreen: CreateProposalScreenIntent()
    data class SelectProposalDuration(val duration: Duration): CreateProposalScreenIntent()
}
