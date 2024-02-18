package com.example.votekt.ui.core

import com.example.votekt.ui.UiErrorMessage

// Global app state, can include auth check result, app lock flag and so on
// consider researching better approach
sealed class AppState {
    object Loading : AppState()

    data class Ready(
        val conditionalNavigation: ConditionalNavigation,
        val requireUnlock: Boolean = false
    ) : AppState()

    data class InitFailure(
        val error: UiErrorMessage
    ) : AppState()
}

data class ConditionalNavigation(
    val requireCreateAccount: Boolean
)
