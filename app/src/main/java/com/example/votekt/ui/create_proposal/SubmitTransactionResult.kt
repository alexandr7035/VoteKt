package com.example.votekt.ui.create_proposal

import com.example.votekt.domain.core.AppError

data class SubmitTransactionResult(
    val isTransactionSubmitted: Boolean,
    val transactionHash: String?,
    val error: AppError?
)
