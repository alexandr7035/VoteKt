package com.example.votekt.ui.feature_app_lock.lock_screen

import androidx.biometric.BiometricPrompt
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult

sealed class LockScreenIntent {
    data class PinFieldChange(val pin: String): LockScreenIntent()
    object BiometricsBtnClicked: LockScreenIntent()

    object RefreshBiometricsAvailability: LockScreenIntent()
    data class AuthenticateWithBiometrics(val data: Pair<BiometricPrompt, BiometricPrompt.PromptInfo>): LockScreenIntent()
    data class ConsumeBiometricAuthResult(val result: BiometricAuthResult) : LockScreenIntent()

    object LogOut: LockScreenIntent()
}