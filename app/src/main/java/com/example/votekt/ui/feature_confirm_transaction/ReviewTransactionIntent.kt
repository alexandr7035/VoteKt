package com.example.votekt.ui.feature_confirm_transaction

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class ReviewTransactionIntent {
    object SubmitTransaction: ReviewTransactionIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ): ReviewTransactionIntent()
    object DismissDialog: ReviewTransactionIntent()
}
