package com.example.votekt.ui.voting_details

import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.votings.Proposal
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ProposalDetailsScreenState(
    val isProposalLoading: Boolean = false,
    val proposal: Proposal? = null,
    val error: AppError? = null,
    val isSelfVoteProcessing: Boolean = false,
    val selfVoteSubmittedEvent: StateEventWithContent<TxHash> = consumed()
)
