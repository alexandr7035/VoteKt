package com.example.votekt.ui.feature.proposals.feed

import com.example.votekt.domain.model.explorer.ExploreType

sealed class ProposalsScreenNavigationEvent {
    object ToAddProposal : ProposalsScreenNavigationEvent()
    data class ToProposal(
        val uuid: String
    ) : ProposalsScreenNavigationEvent()
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : ProposalsScreenNavigationEvent()
}
