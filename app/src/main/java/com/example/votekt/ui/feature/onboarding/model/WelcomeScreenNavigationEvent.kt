package com.example.votekt.ui.feature.onboarding.model

sealed class WelcomeScreenNavigationEvent {
    object ToCreateAccount : WelcomeScreenNavigationEvent()

    object ToRestoreAccount : WelcomeScreenNavigationEvent()
}
