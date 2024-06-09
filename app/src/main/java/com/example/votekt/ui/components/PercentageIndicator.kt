package com.example.votekt.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.theme.VoteKtTheme
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val MAX_ANGLE = 360f
private const val DEFAULT_EMPTY_COLOR = 0xFFF2F2F2

@Composable
fun PercentageIndicator(
    modifier: Modifier = Modifier,
    percentage: Float,
    thickness: Dp = 5.dp,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    emptyColor: Color = Color(DEFAULT_EMPTY_COLOR),
    textSize: TextUnit = 18.sp,
    animationDuration: Duration = 750.milliseconds
) {
    val percentageAnimated = remember { Animatable(0f) }

    LaunchedEffect(percentage) {
        percentageAnimated.animateTo(
            targetValue = percentage * MAX_ANGLE,
            animationSpec = tween(durationMillis = animationDuration.inWholeMilliseconds.toInt())
        )
    }

    @Suppress("MagicNumber")
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val percentageStr = floor(percentage * 100).roundToInt()

        Canvas(
            Modifier.fillMaxSize()
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            val diameter = size.width - thickness.toPx()
            val radius = diameter / 2

            val rectSize = Size(diameter, diameter)
            val rectTopLeft = Offset(centerX - radius, centerY - radius)

            drawArc(
                color = emptyColor,
                startAngle = 0F,
                sweepAngle = MAX_ANGLE,
                useCenter = false,
                topLeft = rectTopLeft,
                size = rectSize,
                style = Stroke(thickness.toPx())
            )

            drawArc(
                color = accentColor,
                startAngle = 0F,
                sweepAngle = percentageAnimated.value,
                useCenter = false,
                topLeft = rectTopLeft,
                size = rectSize,
                style = Stroke(thickness.toPx())
            )
        }

        Text(
            text = stringResource(id = R.string.percentage_template, percentageStr),
            style = TextStyle(
                fontSize = textSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            ),
            maxLines = 1,
            modifier = Modifier.padding(
                vertical = thickness + 4.dp,
                horizontal = thickness + 6.dp,
            ),
        )
    }
}

@Preview
@Composable
fun PercentageIndicator_Preview() {
    VoteKtTheme {
        Surface(
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            PercentageIndicator(
                modifier = Modifier.size(100.dp),
                percentage = 0.81F
            )
        }
    }
}
