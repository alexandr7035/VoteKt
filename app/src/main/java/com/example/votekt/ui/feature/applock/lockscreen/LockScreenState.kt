package com.example.votekt.ui.feature.applock.lockscreen

import com.example.votekt.R
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature.applock.core.AppLockUiState
import com.example.votekt.ui.feature.applock.core.BiometricsPromptUi
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
