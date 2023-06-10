package com.example.votekt.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme

data class VotingBarParams(
    val votesFor: Int,
    val votesAgainst: Int,
    val segmentWidth: Dp = 32.dp,
    val circleSpacingPerc: Float = 0.05f,
)

@Composable
fun VotingBar(params: VotingBarParams) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.background)
    ) { // Adjust the size of the diagram as needed
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2

            val totalVotes = params.votesFor + params.votesAgainst

            val spaceAngle = if (!shouldHideSpacings(params)) {
                360 * params.circleSpacingPerc
            } else {
                0f
            }

            var startAngle = 270 + spaceAngle / 4

            val totalAngle = 360 - spaceAngle
            val sweepAngleFor = (params.votesFor.toFloat() / totalVotes) * totalAngle
            val sweepAngleAgainst = (params.votesAgainst.toFloat() / totalVotes) * totalAngle

            val diameter = size.width - params.segmentWidth.toPx()
            val radius = diameter / 2

            val rectSize = Size(diameter, diameter)
            val rectTopLeft = Offset(centerX - radius, centerY - radius)

            drawArc(
                color = Color.Green,
                startAngle = startAngle,
                sweepAngle = sweepAngleFor,
                useCenter = false,
                topLeft = rectTopLeft,
                size = rectSize,
                style = Stroke(params.segmentWidth.toPx())
            )

            drawArc(
                color = Color.Red,
                startAngle = startAngle + spaceAngle / 2 + sweepAngleFor,
                sweepAngle = sweepAngleAgainst,
                useCenter = false,
                topLeft = rectTopLeft,
                size = rectSize,
                style = Stroke(params.segmentWidth.toPx())
            )
        }
    }
}

private fun shouldHideSpacings(params: VotingBarParams): Boolean {
    return params.votesFor == 0 || params.votesAgainst == 0
}

@Preview(widthDp = 360)
@Composable
fun VotingBar_Preview() {
    VoteKtTheme(darkTheme = false, dynamicColor = false) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            VotingBar(params = VotingBarParams(100, 50))
            VotingBar(params = VotingBarParams(50, 0))
        }
    }
}