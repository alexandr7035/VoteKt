package by.alexandr7035.votekt.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

// FIXME update this component
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
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
            containerColor = buttonColor ?: Color.White,
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
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
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

@Composable
fun TextBtn(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.then(Modifier.wrapContentHeight()),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
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
                TextBtn(onClick = { /*TODO*/ }, modifier = Modifier, text = "Click Me")
            }
        }
    }
}
