package com.example.votekt.ui.feature_proposals.proposal_details

import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalDetailsScreenState(
    val isProposalLoading: Boolean = false,
    val proposal: Proposal? = null,
    val error: UiErrorMessage? = null,
    val isSelfVoteProcessing: Boolean = false,
)
