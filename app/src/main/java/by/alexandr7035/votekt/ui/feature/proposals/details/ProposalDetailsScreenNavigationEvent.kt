package by.alexandr7035.votekt.ui.feature.proposals.details

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class ProposalDetailsScreenNavigationEvent {
    object PopupBack : ProposalDetailsScreenNavigationEvent()
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : ProposalDetailsScreenNavigationEvent()
}
