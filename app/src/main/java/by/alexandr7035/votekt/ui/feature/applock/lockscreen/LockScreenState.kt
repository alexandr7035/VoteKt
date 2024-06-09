package by.alexandr7035.votekt.ui.feature.applock.lockscreen

import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.applock.core.AppLockUiState
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricsPromptUi
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class LockScreenState(
    val uiState: AppLockUiState = AppLockUiState(),
    val biometricsPromptState: BiometricsPromptUi = BiometricsPromptUi(
        title = UiText.StringResource(R.string.unlock_app_biometrics),
        cancelBtnText = UiText.StringResource(R.string.cancel)
    ),
    val showBiometricsPromptEvent: StateEvent = consumed,
    val appUnlockEvent: StateEvent = consumed,
    val logoutEvent: StateEventWithContent<OperationResult<Unit>> = consumed(),
)
