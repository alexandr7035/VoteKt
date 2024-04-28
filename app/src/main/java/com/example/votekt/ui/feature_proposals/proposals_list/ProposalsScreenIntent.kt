package com.example.votekt.ui.feature_proposals.proposals_list

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class ProposalsScreenIntent {
    object EnterScreen: ProposalsScreenIntent()
    data class ChangeControlsVisibility(val isVisible: Boolean): ProposalsScreenIntent()
    data class ProposalClick(val proposalId: String): ProposalsScreenIntent()
    object AddProposalClick: ProposalsScreenIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ): ProposalsScreenIntent()
}
