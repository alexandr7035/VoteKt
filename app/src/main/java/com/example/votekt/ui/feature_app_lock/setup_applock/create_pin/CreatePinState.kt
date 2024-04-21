package com.example.votekt.ui.feature_app_lock.setup_applock.create_pin

import com.example.votekt.ui.feature_app_lock.ui.AppLockUiState
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class CreatePinState(
    val initialPin: String = "",
    val confirmPin: String = "",
    val isConfirmationStage: Boolean = false,
    val uiState: AppLockUiState = AppLockUiState(),
    val pinCreatedEvent: StateEventWithContent<PinCreatedResult> = consumed()
)
