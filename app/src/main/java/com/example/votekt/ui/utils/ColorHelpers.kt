package com.example.votekt.ui.utils

import androidx.compose.ui.graphics.Color
import com.example.votekt.ui.theme.SemanticColors

fun getVoteColor(isVotedFor: Boolean): Color {
    return when (isVotedFor) {
        true -> SemanticColors.AppPositiveColor
        false -> SemanticColors.AppNegativeColor
    }
}
