package com.example.votekt.ui.create_proposal

import com.example.votekt.data.AppError

data class CreateProposalResult(
    val isTransactionSubmitted: Boolean,
    val transactionHash: String?,
    val error: AppError?
)
