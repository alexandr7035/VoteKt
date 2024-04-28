package com.example.votekt.ui.feature_transaction_history.model

import com.example.votekt.domain.model.blockchain_explorer.ExploreType

sealed class TransactionsScreenNavigationEvent {
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ): TransactionsScreenNavigationEvent()
}
