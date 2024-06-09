package by.alexandr7035.votekt.ui.feature.proposals.details

import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.votings.Proposal
import by.alexandr7035.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalDetailsScreenState(
    val isProposalLoading: Boolean = false,
    val proposal: Proposal? = null,
    val error: UiErrorMessage? = null,
    val navigationEvent: StateEventWithContent<ProposalDetailsScreenNavigationEvent> = consumed(),
    val proposalDeploymentFee: Wei? = null,
)
