package com.example.votekt.ui.components.snackbar

import androidx.compose.ui.graphics.Color
import com.example.votekt.ui.theme.ResultColors

sealed class SnackBarMode() {
    object Positive : SnackBarMode()
    object Negative : SnackBarMode()
    object Neutral : SnackBarMode()

    fun getSurfaceColor(): Color {
        return when (this) {
            is Positive -> ResultColors.positiveColor
            is Negative -> ResultColors.negativeColor
            is Neutral -> ResultColors.neutralColor
        }
    }
}
