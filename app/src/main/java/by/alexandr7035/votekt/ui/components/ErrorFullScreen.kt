package by.alexandr7035.votekt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.ui.UiErrorMessage
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

// TODO design
@Composable
fun ErrorFullScreen(
    error: UiErrorMessage,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Text(
            text = error.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = error.message,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 36.dp)
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                onClick = { onRetry.invoke() },
                text = stringResource(R.string.try_again)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun ErrorFullScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
        Surface {
            ErrorFullScreen(error = UiErrorMessage("title", "message"), onRetry = {})
        }
    }
}
