package com.example.votekt.ui.feature_proposals.proposal_details

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalDetailsScreenState(
    val isProposalLoading: Boolean = false,
    val proposal: Proposal? = null,
    val error: UiErrorMessage? = null,
    val navigationEvent: StateEventWithContent<ProposalDetailsScreenNavigationEvent> = consumed(),
    val proposalDeploymentFee: Wei? = null,
)
