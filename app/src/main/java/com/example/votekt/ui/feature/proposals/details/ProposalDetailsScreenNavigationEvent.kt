package com.example.votekt.ui.feature.proposals.details

import com.example.votekt.domain.model.explorer.ExploreType

sealed class ProposalDetailsScreenNavigationEvent {
    object PopupBack : ProposalDetailsScreenNavigationEvent()
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : ProposalDetailsScreenNavigationEvent()
}
