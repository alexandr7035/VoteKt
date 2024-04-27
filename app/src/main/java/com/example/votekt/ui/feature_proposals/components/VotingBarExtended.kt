package com.example.votekt.ui.feature_proposals.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.domain.votings.VotingData
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.utils.getVoteColor
import kotlin.math.roundToInt

@Composable
fun VotingBarExtended(
    votingData: VotingData,
) {
    Card(
        elevation = CardDefaults.cardElevation(Dimensions.defaultCardElevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(Dimensions.defaultCardCorners)
                )
                .padding(
                    vertical = 12.dp,
                    horizontal = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Bar(votingData)
        }
    }
}

@Composable
fun Bar(votingData: VotingData) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VotingLine(
            title = stringResource(id = R.string.supported),
            color = getVoteColor(true),
            percentage = votingData.votesForPercentage
        )

        VotingLine(
            title = stringResource(id = R.string.not_supported),
            color = getVoteColor(false),
            percentage = votingData.votesAgainstPercentage
        )
    }
}

@Composable
fun VotingLine(
    title: String,
    height: Dp = 24.dp,
    percentage: Float,
    color: Color,
    corners: Dp = 16.dp,
    innerPadding: Dp = 4.dp,
    bgColor: Color = Color(0xFFE4E7EB),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(innerPadding),
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )

            val roundedPercentage = remember(percentage) {
                calculateRoundedPercentage(percentage)
            }

            Text(
                modifier = Modifier.padding(innerPadding),
                text = stringResource(id = R.string.percentage_template, roundedPercentage),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }


        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(corners)
                )
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset.Zero,
                    size = Size(
                        width = size.width * percentage,
                        height = size.height,
                    ),
                    cornerRadius = CornerRadius((corners-innerPadding).toPx())
                )
            }
        }
    }
}

private fun calculateRoundedPercentage(percentage: Float) = (percentage * 1000).roundToInt() / 10f

@Preview
@Composable
private fun VotingBarExtended_Preview() {
    ScreenPreview {
        VotingBarExtended(
            votingData = VotingData(
                votesFor = 45,
                votesAgainst = 20,
                selfVote = null,
            )
        )
    }
}