package com.example.votekt.ui.feature_restore_account.model

sealed class RestoreAccountNavigationEvent {
    object GoToSetupAppLock: RestoreAccountNavigationEvent()
}