package com.example.votekt.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.theme.VoteKtTheme

// FIXME update this component
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    buttonColor: Color? = null,
    textColor: Color? = null,
    leadingIcon: @Composable () -> Unit = {}
) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor ?: MaterialTheme.colorScheme.onPrimary,
            containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(Dimensions.buttonCorners),
        contentPadding = PaddingValues(
            horizontal = Dimensions.buttonContentPaddingHorizontal
        ),
        modifier = modifier.then(
            Modifier.defaultMinSize(
                minHeight = Dimensions.buttonMinHeight
            )
        ),
        enabled = enabled,
    ) {
        leadingIcon()
        Text(text = text)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    buttonColor: Color? = null,
    textColor: Color? = null,
    leadingIcon: @Composable () -> Unit = {}
) {
    OutlinedButton(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor ?: MaterialTheme.colorScheme.primary,
            containerColor = buttonColor ?: MaterialTheme.colorScheme.background,
        ),
        shape = RoundedCornerShape(Dimensions.buttonCorners),
        contentPadding = PaddingValues(
            horizontal = Dimensions.buttonContentPaddingHorizontal
        ),
        modifier = modifier.then(
            Modifier.defaultMinSize(
                minHeight = Dimensions.buttonMinHeight
            )
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        enabled = enabled,
    ) {
        leadingIcon()
        Text(text = text)
    }
}

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    buttonColor: Color? = null,
    textColor: Color? = null,
    leadingIcon: @Composable () -> Unit = {}
) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor ?: MaterialTheme.colorScheme.onPrimary,
            containerColor = buttonColor ?: MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(Dimensions.roundedButtonCorners),
        contentPadding = PaddingValues(
            horizontal = Dimensions.buttonContentPaddingHorizontal
        ),
        modifier = modifier.then(
            Modifier.defaultMinSize(
                minHeight = Dimensions.roundedButtonHeight
            )
        ),
        enabled = enabled,
    ) {
        leadingIcon()
        Text(text = text)
    }
}

@Preview(widthDp = 360, heightDp = 560)
@Composable
fun PrimaryButton_Preview() {
    VoteKtTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(Modifier.fillMaxSize()) {
                PrimaryButton(text = "Click Me", onClick = {})
                SecondaryButton(text = "Click Me", onClick = {})
                RoundedButton(text = "Click Me", onClick = {})
            }
        }
    }
}