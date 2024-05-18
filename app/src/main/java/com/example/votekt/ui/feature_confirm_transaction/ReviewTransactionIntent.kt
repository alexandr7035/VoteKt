package com.example.votekt.ui.feature_confirm_transaction

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class ReviewTransactionIntent {
    object SubmitTransactionClick: ReviewTransactionIntent()
    data class BiometricAuthUnlock(val isSuccessful: Boolean): ReviewTransactionIntent()

    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ): ReviewTransactionIntent()
    object DismissDialog: ReviewTransactionIntent()
}
