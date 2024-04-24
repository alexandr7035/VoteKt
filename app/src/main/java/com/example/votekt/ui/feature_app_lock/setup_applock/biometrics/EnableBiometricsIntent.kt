package com.example.votekt.ui.feature_app_lock.setup_applock.biometrics

import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult

sealed class EnableBiometricsIntent {
    object RefreshBiometricsAvailability: EnableBiometricsIntent()
    data class EnterScreen(val pinToEncrypt: String): EnableBiometricsIntent()
    data class AuthenticateWithBiometrics(val data: Pair<BiometricPrompt, PromptInfo>): EnableBiometricsIntent()
    data class ConsumeAuthResult(val result: BiometricAuthResult) : EnableBiometricsIntent()
}
