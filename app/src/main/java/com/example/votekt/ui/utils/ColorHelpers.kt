package com.example.votekt.ui.utils

import androidx.compose.ui.graphics.Color

// TODO replace with styles
fun getVoteColor(isVotedFor: Boolean): Color {
    return when (isVotedFor) {
        true -> Color(0xFF00B16E)
        false -> Color(0xFFEB3A61)
    }
}