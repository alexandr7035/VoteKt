package com.example.votekt.ui.feature.account.restore.model

sealed class RestoreAccountNavigationEvent {
    object GoToSetupAppLock : RestoreAccountNavigationEvent()
}
