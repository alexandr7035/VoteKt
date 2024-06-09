package by.alexandr7035.votekt.ui.feature.transactions.review

import by.alexandr7035.votekt.domain.model.explorer.ExploreType

sealed class ReviewTransactionIntent {
    object SubmitTransactionClick : ReviewTransactionIntent()
    data class BiometricAuthUnlock(val isSuccessful: Boolean) : ReviewTransactionIntent()

    data class ExplorerUrlClick(
        val payload: String,
        val exploreType: ExploreType,
    ) : ReviewTransactionIntent()
    object DismissDialog : ReviewTransactionIntent()
}
