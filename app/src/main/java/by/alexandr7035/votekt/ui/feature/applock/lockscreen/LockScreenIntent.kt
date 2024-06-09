package by.alexandr7035.votekt.ui.feature.applock.lockscreen

import androidx.biometric.BiometricPrompt
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricAuthResult

sealed class LockScreenIntent {
    data class PinFieldChange(val pin: String) : LockScreenIntent()
    object BiometricsBtnClicked : LockScreenIntent()

    object RefreshBiometricsAvailability : LockScreenIntent()
    data class AuthenticateWithBiometrics(
        val data: Pair<BiometricPrompt, BiometricPrompt.PromptInfo>
    ) : LockScreenIntent()
    data class ConsumeBiometricAuthResult(val result: BiometricAuthResult) : LockScreenIntent()

    object LogOut : LockScreenIntent()
}
