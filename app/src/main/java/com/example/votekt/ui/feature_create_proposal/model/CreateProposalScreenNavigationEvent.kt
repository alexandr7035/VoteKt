package com.example.votekt.ui.feature_create_proposal.model

import com.example.votekt.domain.core.Uuid

sealed class CreateProposalScreenNavigationEvent {
    object PopupBack: CreateProposalScreenNavigationEvent()
    data class NavigateToProposal(val proposalId: Uuid): CreateProposalScreenNavigationEvent()
}