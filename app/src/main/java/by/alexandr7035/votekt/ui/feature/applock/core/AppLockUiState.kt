package by.alexandr7035.votekt.ui.feature.applock.core

import by.alexandr7035.votekt.ui.core.resources.UiText

data class AppLockUiState(
    val prompt: UiText = UiText.DynamicString(""),
    val pinLength: Int = 4,
    val pinValue: String = "",
    val error: UiText? = null,
    val isLoading: Boolean = false,
    val showBiometricsBtn: Boolean = false,
)
