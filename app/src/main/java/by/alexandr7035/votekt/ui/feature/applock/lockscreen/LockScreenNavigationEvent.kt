package by.alexandr7035.votekt.ui.feature.applock.lockscreen

sealed class LockScreenNavigationEvent {
    object PopupBack : LockScreenNavigationEvent()
    object GoToWelcome : LockScreenNavigationEvent()
}
