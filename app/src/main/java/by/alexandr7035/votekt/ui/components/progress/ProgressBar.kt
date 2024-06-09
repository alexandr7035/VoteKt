package by.alexandr7035.votekt.ui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import by.alexandr7035.votekt.ui.components.DotsProgressIndicator
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

private const val DEFAULT_BACKGROUND = 0x80808E8E

@Composable
fun FullscreenProgressBar(
    backgroundColor: Color? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor ?: Color(DEFAULT_BACKGROUND))
            .pointerInput(Unit) {
                // Skip touch events
            },
        contentAlignment = Alignment.Center
    ) {
        DotsProgressIndicator()
    }
}

@Preview
@Composable
fun FullscreenProgressBar_Ui() {
    VoteKtTheme {
        Surface {
            FullscreenProgressBar()
        }
    }
}
