package com.example.votekt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.R
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.theme.Dimensions

enum class TipType {
    NORMAL,
    WARMING
}

private val tipShape = RoundedCornerShape(16.dp)

@Composable
fun TipView(
    text: UiText,
    modifier: Modifier = Modifier,
    tipType: TipType = TipType.NORMAL,
) {
    Row(
        modifier = modifier.then(
            Modifier
                .background(
                    color = when (tipType) {
                        TipType.NORMAL -> Color(0xC063B3D7)
                        TipType.WARMING -> Color(0xC0FF9462)
                    },
                    shape = tipShape,
                )
                .clip(tipShape)
                .padding(
                    horizontal = Dimensions.tipPaddingHorizontal,
                    vertical = Dimensions.tipPaddingVertical,
                )
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.tipIconSpacing)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null,
            modifier = Modifier.size(Dimensions.tipIconSize),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = text.asString(),
            style = TextStyle(
                color = Color.White,
                fontSize = Dimensions.tipFontSize,
                fontWeight = FontWeight.SemiBold,
            )
        )
    }
}

@Composable
@Preview
private fun Tip_Preview() {
    ScreenPreview() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TipView(text = UiText.DynamicString("This is a sample tip"))
        }
    }
}