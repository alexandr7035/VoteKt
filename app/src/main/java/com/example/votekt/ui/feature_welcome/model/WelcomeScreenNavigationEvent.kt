package com.example.votekt.ui.feature_welcome.model

sealed class WelcomeScreenNavigationEvent {
    object ToCreateAccount: WelcomeScreenNavigationEvent()

    object ToRestoreAccount: WelcomeScreenNavigationEvent()
}