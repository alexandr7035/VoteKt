package com.example.votekt.ui.create_proposal

import com.example.votekt.core.config.ProposalConfig
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class CreateProposalScreenState(
    val titleMaxLength: Int = ProposalConfig.titleMaxLength,
    val descMaxLength: Int = ProposalConfig.descMaxLength,
    val isLoading: Boolean = false,
    val submitProposalEvent: StateEventWithContent<SubmitTransactionResult> = consumed()
)
