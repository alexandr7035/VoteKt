package com.example.votekt.ui.components.snackbar

import androidx.compose.ui.graphics.Color

sealed class SnackBarMode {
    object Positive : SnackBarMode()
    object Negative : SnackBarMode()
    object Neutral : SnackBarMode()

    fun getSurfaceColor(): Color {
        return when (this) {
            is Positive -> SnackbarColors.positiveColor
            is Negative -> SnackbarColors.negativeColor
            is Neutral -> SnackbarColors.neutralColor
        }
    }
}
