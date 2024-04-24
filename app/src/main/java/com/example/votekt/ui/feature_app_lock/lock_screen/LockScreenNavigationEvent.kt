package com.example.votekt.ui.feature_app_lock.lock_screen

sealed class LockScreenNavigationEvent {
    object PopupBack: LockScreenNavigationEvent()
    object GoToWelcome: LockScreenNavigationEvent()
}