package com.example.votekt.ui.feature_app_lock.setup_applock.biometrics

import com.example.votekt.domain.security.model.BiometricsAvailability
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult
import com.example.votekt.ui.feature_app_lock.biometrics.BiometricsPromptUi
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class EnableBiometricsState(
    val pinToEncrypt: String? = null,
    val prompt: BiometricsPromptUi,
    val biometricsAvailability: BiometricsAvailability,
    val authResultEvent: StateEventWithContent<BiometricAuthResult> = consumed()
)
