package by.alexandr7035.votekt.ui.components.snackbar

import androidx.compose.ui.graphics.Color

sealed class SnackBarMode {
    object Positive : SnackBarMode()
    object Negative : SnackBarMode()
    object Neutral : SnackBarMode()

    fun getSurfaceColor(): Color {
        return when (this) {
            is Positive -> SnackBarColors.positiveColor
            is Negative -> SnackBarColors.negativeColor
            is Neutral -> SnackBarColors.neutralColor
        }
    }
}
