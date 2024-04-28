package com.example.votekt.ui.components.web3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.theme.VoteKtTheme

@Composable
fun ExplorableText(
    text: String,
    onClick: () -> Unit,
    color: Color = Color.Gray,
    fontSize: TextUnit = 16.sp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ClickableText(
            text = AnnotatedString(text),
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                color = color,
                textDecoration = TextDecoration.Underline
            ),
            onClick = {
                onClick()
            },
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_link),
            contentDescription = null,
            tint = color,
        )
    }


}

@Composable
@Preview
fun ExplorableText_Preview() {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            ExplorableText(
                text = "0x1234....000",
                onClick = {}
            )
        }
    }
}