package by.alexandr7035.votekt.ui.feature.onboarding.model

sealed class WelcomeScreenIntent {
    object CreateAccountClick : WelcomeScreenIntent()

    object AlreadyHaveAccountClick : WelcomeScreenIntent()
}
