package com.example.votekt.ui.feature.applock.setup.biometrics

import com.example.votekt.domain.security.model.BiometricsAvailability
import com.example.votekt.ui.feature.applock.core.BiometricAuthResult
import com.example.votekt.ui.feature.applock.core.BiometricsPromptUi
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class EnableBiometricsState(
    val pinToEncrypt: String? = null,
    val prompt: BiometricsPromptUi,
    val biometricsAvailability: BiometricsAvailability,
    val authResultEvent: StateEventWithContent<BiometricAuthResult> = consumed()
)
