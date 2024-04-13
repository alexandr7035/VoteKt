package com.example.votekt.ui.feature_welcome.model

sealed class WelcomeScreenIntent {
    object CreateAccountClick: WelcomeScreenIntent()

    object AlreadyHaveAccountClick: WelcomeScreenIntent()
}
