package com.example.votekt.ui.feature_create_proposal.model

import com.example.votekt.ui.core.resources.UiText
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import kotlin.time.Duration

data class CreateProposalScreenState(
    val titleMaxLength: Int = 0,
    val descMaxLength: Int = 0,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val proposalDuration: Duration = Duration.ZERO,
    val submitProposalEvent: StateEventWithContent<CreateProposalResult> = consumed(),
    val navigationEvent: StateEventWithContent<CreateProposalScreenNavigationEvent> = consumed(),
    val errorEvent: StateEventWithContent<UiText> = consumed(),
) {
    val isSaveButtonEnabled: Boolean
        get() = this.title.isNotBlank() && this.description.isNotBlank()
}
