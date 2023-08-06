package com.example.votekt.ui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme

@Composable
fun FullscreenProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x8E808E8E))
            .pointerInput(Unit) {
                // Skip touch events
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 6.dp,
            modifier = Modifier.then(Modifier.size(56.dp))
        )
    }
}

@Preview
@Composable
fun FullscreenProgressBar_Ui() {
    VoteKtTheme {
        Surface() {
            FullscreenProgressBar()
        }
    }
}