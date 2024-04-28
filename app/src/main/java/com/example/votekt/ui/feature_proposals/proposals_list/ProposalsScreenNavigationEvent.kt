package com.example.votekt.ui.feature_proposals.proposals_list

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class ProposalsScreenNavigationEvent {
    object ToAddProposal: ProposalsScreenNavigationEvent()
    data class ToProposal(
        val uuid: String
    ): ProposalsScreenNavigationEvent()
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ): ProposalsScreenNavigationEvent()
}
