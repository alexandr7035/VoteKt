package by.alexandr7035.votekt.ui.feature.proposals.create.model

import by.alexandr7035.votekt.domain.core.Uuid

sealed class CreateProposalScreenNavigationEvent {
    object PopupBack : CreateProposalScreenNavigationEvent()
    data class NavigateToProposal(val proposalId: Uuid) : CreateProposalScreenNavigationEvent()
}
