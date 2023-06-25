package com.example.votekt.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(horizontal = 36.dp),
        modifier = modifier.then(Modifier
            .defaultMinSize(minHeight = 48.dp))
    ) {
        Text(text = text)
    }
}


@Preview(widthDp = 360, heightDp = 560)
@Composable
fun PrimaryButton_Preview() {
    VoteKtTheme(darkTheme = true) {
        Column(Modifier.fillMaxSize()) {
            PrimaryButton(text = "Click Me", onClick = {})
        }
    }
}