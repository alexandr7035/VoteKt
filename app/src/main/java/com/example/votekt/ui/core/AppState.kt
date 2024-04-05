package com.example.votekt.ui.core

import com.example.votekt.ui.UiErrorMessage
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionDataUi

// Global app state, can include auth check result, app lock flag and so on
// consider researching better approach
sealed class AppState {
    object Loading : AppState()

    data class Ready(
        val conditionalNavigation: ConditionalNavigation,
        val requireUnlock: Boolean = false,
        val txConfirmationState: ReviewTransactionDataUi? = null,
    ) : AppState()

    data class InitFailure(
        val error: UiErrorMessage
    ) : AppState()
}

data class ConditionalNavigation(
    val requireCreateAccount: Boolean
)

fun AppState.requiresTxConfirmation(): Boolean {
    return this is AppState.Ready && this.txConfirmationState != null
}
