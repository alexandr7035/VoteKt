package com.example.votekt.ui.votings_list

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class CreateProposalState(
    val isCreateCompleted: StateEventWithContent<Boolean> = consumed()
)
