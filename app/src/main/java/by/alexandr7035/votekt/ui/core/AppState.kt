package by.alexandr7035.votekt.ui.core

import by.alexandr7035.votekt.ui.UiErrorMessage
import by.alexandr7035.votekt.ui.feature.transactions.review.ReviewTransactionDataUi
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

// Global app state, can include auth check result, app lock flag and so on
// consider researching better approach
data class AppState(
    val isLoading: Boolean = true,
    val conditionalNavigation: ConditionalNavigation = ConditionalNavigation(),
    val appError: UiErrorMessage? = null,
    val txConfirmationState: ReviewTransactionDataUi? = null,
    val appConnectionState: AppConnectionState = AppConnectionState.CONNECTING,
    val openExplorerEvent: StateEventWithContent<String> = consumed()
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
