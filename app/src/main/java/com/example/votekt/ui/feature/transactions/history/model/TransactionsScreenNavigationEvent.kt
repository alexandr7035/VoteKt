package com.example.votekt.ui.feature.transactions.history.model

import com.example.votekt.domain.model.explorer.ExploreType

sealed class TransactionsScreenNavigationEvent {
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : TransactionsScreenNavigationEvent()
}
