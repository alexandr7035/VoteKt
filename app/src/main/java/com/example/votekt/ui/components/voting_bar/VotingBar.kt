package com.example.votekt.ui.components.voting_bar

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.theme.VoteKtTheme
import kotlin.math.roundToInt

data class VotingBarParams(
    val votesFor: Int,
    val votesAgainst: Int,
    val segmentWidth: Dp = 32.dp,
    val circleSpacingPerc: Float = 0.05f,
) {
    fun votingNotEmpty(): Boolean {
        return votesAgainst + votesFor != 0
    }

    // Include 50/50 here
    fun isFor(): Boolean {
        return votesFor > votesAgainst
    }

    fun getVoteColor(): Color {
        return if (votingNotEmpty()) {
            if (isFor()) {
                VotingColors.forColor
            } else {
                VotingColors.againstColor
            }
        } else {
            Color.LightGray
        }
    }
}

@Composable
fun VotingBar(params: VotingBarParams) {
    Row(
        Modifier
            .background(MaterialTheme.colorScheme.background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        VotingMetric(isFor = false, votersCount = params.votesAgainst)
        VotingBarCircle(params = params, modifier = Modifier.weight(1f))
        VotingMetric(isFor = true, votersCount = params.votesFor)
    }
}

@Composable
fun VotingBarCircle(params: VotingBarParams, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.background)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) { // Adjust the size of the diagram as needed
        Log.d("TEST", params.toString())

        if (params.votingNotEmpty()) {
            VotingCircle_Voted(params = params)
        }
        else {
            VotingCircle_Empty(params = params)
        }
    }
}

@Composable
private fun VotingCircle_Voted(
    params: VotingBarParams
) {

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

    val forArcAnimated = remember { Animatable(0f) }
    val againstArcAnimated = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        againstArcAnimated.animateTo(
            targetValue = sweepAngleAgainst,
            animationSpec = tween(durationMillis = 500)
        )
    }

    LaunchedEffect(Unit) {
        forArcAnimated.animateTo(
            targetValue = sweepAngleFor,
            animationSpec = tween(durationMillis = 500)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        val diameter = size.width - params.segmentWidth.toPx()
        val radius = diameter / 2

        val rectSize = Size(diameter, diameter)
        val rectTopLeft = Offset(centerX - radius, centerY - radius)

        drawArc(
            color = VotingColors.forColor,
            startAngle = startAngle,
            sweepAngle = forArcAnimated.value,
            useCenter = false,
            topLeft = rectTopLeft,
            size = rectSize,
            style = Stroke(params.segmentWidth.toPx())
        )

        drawArc(
            color = VotingColors.againstColor,
            startAngle = startAngle + spaceAngle / 2 + sweepAngleFor,
            sweepAngle = againstArcAnimated.value,
            useCenter = false,
            topLeft = rectTopLeft,
            size = rectSize,
            style = Stroke(params.segmentWidth.toPx())
        )
    }

    Column() {
        var icon: Painter
        var color: Color
        var perc: Float

        if (totalVotes > 0) {
            // Include 50/50 here
            val isFor = params.votesFor >= params.votesAgainst

            if (isFor) {
                icon = painterResource(id = R.drawable.thumb_up)
                color = VotingColors.forColor
                perc = params.votesFor / totalVotes.toFloat()
            } else {
                icon = painterResource(id = R.drawable.thumb_down)
                color = VotingColors.againstColor
                perc = params.votesAgainst / totalVotes.toFloat()
            }
        } else {
            icon = painterResource(id = R.drawable.thumb_down)
            color = Color.LightGray
            perc = 0F
        }

        Log.d("TEST", perc.toString())
        val prettyPerc = "${(perc * 100).roundToInt()}%"

        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            colorFilter = ColorFilter.tint(color)
        )

        Text(text = prettyPerc, fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun VotingCircle_Empty(params: VotingBarParams) {

    Canvas(modifier = Modifier.fillMaxSize()) {

        val centerX = size.width / 2
        val centerY = size.height / 2

        val diameter = size.width - params.segmentWidth.toPx()
        val radius = diameter / 2

        val rectSize = Size(diameter, diameter)
        val rectTopLeft = Offset(centerX - radius, centerY - radius)

        drawArc(
            color = params.getVoteColor(),
            startAngle = 0F,
            sweepAngle = 360F,
            useCenter = false,
            topLeft = rectTopLeft,
            size = rectSize,
            style = Stroke(params.segmentWidth.toPx())
        )
    }

}

@Composable
private fun VotingMetric(isFor: Boolean, votersCount: Int) {

    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var icon: Painter
        var color: Color
        var label: String

        if (isFor) {
            icon = painterResource(id = R.drawable.thumb_up)
            color = VotingColors.forColor
            label = "Yes"
        } else {
            icon = painterResource(id = R.drawable.thumb_down)
            color = VotingColors.againstColor
            label = "No"
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(color)
            )
            Text(text = label, fontSize = 18.sp)
        }


        Text(
            text = votersCount.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun shouldHideSpacings(params: VotingBarParams): Boolean {
    return params.votesFor == 0 || params.votesAgainst == 0
}

private object VotingColors {
    val forColor = Color(0xFF00B16E)
    val againstColor = Color(0xFFEB3A61)
}

@Preview(widthDp = 360)
@Composable
fun VotingBar_Preview() {
    VoteKtTheme(darkTheme = false, dynamicColor = false) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            VotingBarCircle(params = VotingBarParams(100, 50))
            VotingBar(
                params = VotingBarParams(
                    votesAgainst = 50,
                    votesFor = 45,
                    segmentWidth = 16.dp,
                    circleSpacingPerc = 0.025f
                )
            )

            VotingBar(
                params = VotingBarParams(
                    votesAgainst = 0,
                    votesFor = 0,
                    segmentWidth = 16.dp,
                    circleSpacingPerc = 0.025f
                )
            )
        }
    }
}