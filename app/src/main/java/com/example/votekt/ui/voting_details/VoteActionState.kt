package com.example.votekt.ui.voting_details

import com.example.votekt.ui.create_proposal.SubmitTransactionResult
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class VoteActionState(
    val isLoading: Boolean = false,
    val voteTxSubmittedEvent: StateEventWithContent<SubmitTransactionResult> = consumed()
)
