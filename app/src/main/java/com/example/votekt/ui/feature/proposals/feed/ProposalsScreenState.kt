package com.example.votekt.ui.feature.proposals.feed

import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalsScreenState(
    val isLoading: Boolean = false,
    val proposals: List<Proposal> = emptyList(),
    val error: UiErrorMessage? = null,
    val controlsAreVisible: Boolean = true,
    val navigationEvent: StateEventWithContent<ProposalsScreenNavigationEvent> = consumed()
)
