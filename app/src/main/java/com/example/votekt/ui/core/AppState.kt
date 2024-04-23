package com.example.votekt.ui.core

import com.example.votekt.ui.UiErrorMessage
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionDataUi

// Global app state, can include auth check result, app lock flag and so on
// consider researching better approach
data class AppState(
    val isLoading: Boolean = true,
    val conditionalNavigation: ConditionalNavigation = ConditionalNavigation(),
    val appError: UiErrorMessage? = null,
    val txConfirmationState: ReviewTransactionDataUi? = null,
    val appConnectionState: AppConnectionState = AppConnectionState.CONNECTING,
)

data class ConditionalNavigation(
    val requireCreateAccount: Boolean = false,
    val requireUnlockApp: Boolean = true,
    val requireCreateAppLock: Boolean = false,
)

enum class AppConnectionState {
    CONNECTING,
    ONLINE,
    OFFLINE
}

fun AppState.isLoggedIn() = this.conditionalNavigation.requireCreateAccount.not()

fun AppState.isAppLocked() = this.conditionalNavigation.requireUnlockApp

fun AppState.isOffline() = this.appConnectionState != AppConnectionState.ONLINE

fun AppState.requiresTxConfirmation(): Boolean {
    return this.txConfirmationState != null
}
