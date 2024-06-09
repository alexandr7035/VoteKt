package by.alexandr7035.votekt.ui.feature.onboarding.model

sealed class WelcomeScreenNavigationEvent {
    object ToCreateAccount : WelcomeScreenNavigationEvent()

    object ToRestoreAccount : WelcomeScreenNavigationEvent()
}
