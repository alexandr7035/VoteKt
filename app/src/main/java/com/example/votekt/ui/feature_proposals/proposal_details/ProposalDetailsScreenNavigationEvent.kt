package com.example.votekt.ui.feature_proposals.proposal_details

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class ProposalDetailsScreenNavigationEvent {
    object PopupBack: ProposalDetailsScreenNavigationEvent()
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ): ProposalDetailsScreenNavigationEvent()
}
