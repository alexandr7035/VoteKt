package by.alexandr7035.votekt.ui.feature.applock.setup.biometrics

import by.alexandr7035.votekt.domain.security.model.BiometricsAvailability
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricAuthResult
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricsPromptUi
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class EnableBiometricsState(
    val pinToEncrypt: String? = null,
    val prompt: BiometricsPromptUi,
    val biometricsAvailability: BiometricsAvailability,
    val authResultEvent: StateEventWithContent<BiometricAuthResult> = consumed()
)
