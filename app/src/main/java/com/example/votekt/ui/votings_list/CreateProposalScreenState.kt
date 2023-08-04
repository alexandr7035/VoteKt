package com.example.votekt.ui.votings_list

import com.example.votekt.core.config.ProposalConfig
import com.example.votekt.data.model.ProposalDuration
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class CreateProposalScreenState(
    val titleMaxLength: Int = ProposalConfig.titleMaxLength,
    val descMaxLength: Int = ProposalConfig.descMaxLength,
    val isCreateCompleted: StateEventWithContent<Boolean> = consumed()
)
