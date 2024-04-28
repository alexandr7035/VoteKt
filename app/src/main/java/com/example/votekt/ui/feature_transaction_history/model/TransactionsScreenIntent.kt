package com.example.votekt.ui.feature_transaction_history.model

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class TransactionsScreenIntent {
    object ClearClick: TransactionsScreenIntent()
    object RetryErrorClick: TransactionsScreenIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ): TransactionsScreenIntent()
}
