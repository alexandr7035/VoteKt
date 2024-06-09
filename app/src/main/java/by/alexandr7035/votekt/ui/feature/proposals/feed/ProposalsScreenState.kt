package by.alexandr7035.votekt.ui.feature.proposals.feed

import by.alexandr7035.votekt.domain.votings.Proposal
import by.alexandr7035.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalsScreenState(
    val isLoading: Boolean = false,
    val proposals: List<Proposal> = emptyList(),
    val error: UiErrorMessage? = null,
    val controlsAreVisible: Boolean = true,
    val navigationEvent: StateEventWithContent<ProposalsScreenNavigationEvent> = consumed()
)
