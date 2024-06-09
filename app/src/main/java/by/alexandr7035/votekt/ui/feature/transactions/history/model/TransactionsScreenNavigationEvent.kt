package by.alexandr7035.votekt.ui.feature.transactions.history.model

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class TransactionsScreenNavigationEvent {
    data class ToExplorer(
        val payload: String,
        val exploreType: ExploreType,
    ) : TransactionsScreenNavigationEvent()
}
