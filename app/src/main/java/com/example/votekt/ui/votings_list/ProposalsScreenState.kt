package com.example.votekt.ui.votings_list

import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.UiErrorMessage

data class ProposalsScreenState(
    val isLoading: Boolean = false,
    val proposals: List<Proposal> = emptyList(),
    val error: UiErrorMessage? = null,
)
