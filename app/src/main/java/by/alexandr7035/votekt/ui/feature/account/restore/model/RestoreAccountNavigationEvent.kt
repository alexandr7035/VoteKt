package by.alexandr7035.votekt.ui.feature.account.restore.model

sealed class RestoreAccountNavigationEvent {
    object GoToSetupAppLock : RestoreAccountNavigationEvent()
}
