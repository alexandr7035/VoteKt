package com.example.votekt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getVoteColor
import kotlin.math.roundToInt

@Composable
fun HorizontalVotingBar(
    params: VotingBarParams,
    modifier: Modifier
) {
    val totalVotes = params.votesFor + params.votesAgainst
    val proportionFor = params.votesFor.toFloat() / totalVotes
    val proportionAgainst = params.votesAgainst.toFloat() / totalVotes

    Row(modifier) {
        if (totalVotes == 0) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.LightGray,
                    )
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        if (proportionFor > 0F) {
            Box(
                modifier = Modifier
                    .clipToBounds()

                    .background(
                        color = getVoteColor(isVotedFor = true),
//                        shape = RoundedCornerShape(
//                            topStart = 16.dp, bottomStart = 16.dp
//                        )
                    )
                    .weight(proportionFor)
                    .fillMaxHeight()
            )
        }

        if (proportionAgainst > 0F) {
            Box(
                modifier = Modifier
                    .clipToBounds()

                    .background(
                        color = getVoteColor(isVotedFor = false),
//                        shape = RoundedCornerShape(
//                            topEnd = 16.dp, bottomEnd = 16.dp
//                        )
                    )
                    .weight(proportionAgainst)
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
                    params = VotingBarParams(10, 5), modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    params = VotingBarParams(1, 50), modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    params = VotingBarParams(1, 0), modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    params = VotingBarParams(0, 0), modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }


        }
    }
}