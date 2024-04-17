package com.example.votekt.ui.components.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.votekt.ui.theme.VoteKtTheme

@Composable
fun ScreenPreview(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    VoteKtTheme(darkTheme = darkTheme) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}