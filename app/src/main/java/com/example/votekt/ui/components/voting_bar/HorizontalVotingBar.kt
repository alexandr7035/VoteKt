package com.example.votekt.ui.components.voting_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getVoteColor

@Composable
fun HorizontalVotingBar(
    votesFor: Int,
    votesAgainst: Int,
    modifier: Modifier,
    corners: Dp = 16.dp
) {
    val totalVotes = votesFor + votesAgainst
    val proportionFor = votesFor.toFloat() / totalVotes
    val proportionAgainst = votesAgainst.toFloat() / totalVotes

    Row(
        modifier = modifier.then(
            Modifier.clip(
                shape = RoundedCornerShape(corners),
            )
        )
    ) {
        if (totalVotes == 0) {
            Box(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        if (proportionAgainst > 0F) {
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .background(color = getVoteColor(isVotedFor = false))
                    .weight(proportionAgainst)
                    .fillMaxHeight()
            )
        }

        if (proportionFor > 0F) {
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .background(color = getVoteColor(isVotedFor = true))
                    .weight(proportionFor)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
fun HorizontalVotingBar_Preview() {
    VoteKtTheme() {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {

            Column(Modifier.wrapContentHeight(), Arrangement.spacedBy(12.dp)) {
                HorizontalVotingBar(
                    votesFor = 10, votesAgainst = 5, modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votesFor = 1, votesAgainst = 50, modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votesFor = 1, votesAgainst = 0, modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votesFor = 0, votesAgainst = 0, modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }
        }
    }
}