package com.example.votekt.ui.feature_proposals.proposals_list

sealed class ProposalsScreenIntent {
    data class ChangeControlsVisibility(val isVisible: Boolean): ProposalsScreenIntent()
}
