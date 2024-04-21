package com.example.votekt.ui.feature_app_lock.ui

import androidx.biometric.BiometricPrompt

sealed class BiometricAuthResult {
    data class Success(val authData: BiometricPrompt.AuthenticationResult): BiometricAuthResult()
    data class Failure(val error: String): BiometricAuthResult()
}
