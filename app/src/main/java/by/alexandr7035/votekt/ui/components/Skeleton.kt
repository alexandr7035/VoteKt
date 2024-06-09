package by.alexandr7035.votekt.ui.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import by.alexandr7035.votekt.ui.theme.VoteKtTheme

@Composable
fun SkeletonShape(modifier: Modifier) {
    val startColor = Color.LightGray
    val endColor = Color.Gray

    val color = remember { Animatable(startColor) }
    SkeletonColorAnimation(color = color, endColor = endColor)

    Box(
        modifier = modifier.then(
            Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color.value)
        )
    )
}

@Composable
fun SkeletonCircle(size: Dp) {
    val startColor = Color.LightGray
    val endColor = Color.Gray

    val color = remember { Animatable(startColor) }
    SkeletonColorAnimation(color = color, endColor = endColor)

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color.value)
            .size(size)
    )
}

@Composable
private fun SkeletonColorAnimation(color: Animatable<Color, AnimationVector4D>, endColor: Color) {
    LaunchedEffect(Unit) {
        color.animateTo(
            endColor,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    delayMillis = 250
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
}

@Composable
@Preview(widthDp = 300, heightDp = 56)
fun SkeletonContainer_Preview() {
    VoteKtTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkeletonCircle(56.dp)

            SkeletonShape(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}
