package by.alexandr7035.votekt.ui.feature.proposals.feed

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class ProposalsScreenIntent {
    object EnterScreen : ProposalsScreenIntent()
    data class ChangeControlsVisibility(val isVisible: Boolean) : ProposalsScreenIntent()
    data class ProposalClick(val proposalId: String) : ProposalsScreenIntent()
    object AddProposalClick : ProposalsScreenIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ) : ProposalsScreenIntent()
}
