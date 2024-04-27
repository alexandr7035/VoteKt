package com.example.votekt.ui.feature_create_proposal.model

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import kotlin.time.Duration

data class CreateProposalScreenState(
    val titleMaxLength: Int = 0,
    val descMaxLength: Int = 0,
    val isLoading: Boolean = false,
    val proposalDuration: Duration = Duration.ZERO,
    val submitProposalEvent: StateEventWithContent<CreateProposalResult> = consumed(),
)
