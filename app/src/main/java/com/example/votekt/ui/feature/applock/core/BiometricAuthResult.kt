package com.example.votekt.ui.feature.applock.core

import androidx.biometric.BiometricPrompt

sealed class BiometricAuthResult {
    data class Success(val authData: BiometricPrompt.AuthenticationResult) : BiometricAuthResult()
    data class Failure(val error: String) : BiometricAuthResult()
}
