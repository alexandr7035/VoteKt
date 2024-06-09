package by.alexandr7035.votekt.ui.feature.transactions.history.model

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class TransactionsScreenIntent {
    object ClearClick : TransactionsScreenIntent()
    object RetryErrorClick : TransactionsScreenIntent()
    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ) : TransactionsScreenIntent()
}
