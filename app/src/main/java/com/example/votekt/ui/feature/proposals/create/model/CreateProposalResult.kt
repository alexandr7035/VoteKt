package com.example.votekt.ui.feature.proposals.create.model

import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.core.Uuid

data class CreateProposalResult(
    val proposalUuid: Uuid?,
    val error: AppError?,
)
