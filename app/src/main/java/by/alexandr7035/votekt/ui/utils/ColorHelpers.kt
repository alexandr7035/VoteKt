package by.alexandr7035.votekt.ui.utils

import androidx.compose.ui.graphics.Color
import by.alexandr7035.votekt.ui.theme.SemanticColors

fun getVoteColor(isVotedFor: Boolean): Color {
    return when (isVotedFor) {
        true -> SemanticColors.AppPositiveColor
        false -> SemanticColors.AppNegativeColor
    }
}
