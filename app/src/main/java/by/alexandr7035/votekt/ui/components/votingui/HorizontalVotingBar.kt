package by.alexandr7035.votekt.ui.components.votingui

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
import by.alexandr7035.votekt.domain.model.proposal.VotingData
import by.alexandr7035.votekt.ui.theme.VoteKtTheme
import by.alexandr7035.votekt.ui.utils.getVoteColor

@Composable
fun HorizontalVotingBar(
    votingData: VotingData,
    modifier: Modifier,
    corners: Dp = 16.dp
) {
    Row(
        modifier = modifier.then(
            Modifier.clip(
                shape = RoundedCornerShape(corners),
            )
        )
    ) {
        if (!votingData.hasVotes()) {
            Box(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        if (votingData.votesAgainstPercentage > 0) {
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .background(color = getVoteColor(isVotedFor = false))
                    .weight(votingData.votesAgainstPercentage)
                    .fillMaxHeight()
            )
        }

        if (votingData.votesForPercentage > 0) {
            Box(
                modifier = Modifier
                    .clipToBounds()
                    .background(color = getVoteColor(isVotedFor = true))
                    .weight(votingData.votesForPercentage)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
fun HorizontalVotingBar_Preview() {
    VoteKtTheme {
        Surface(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Column(Modifier.wrapContentHeight(), Arrangement.spacedBy(12.dp)) {
                HorizontalVotingBar(
                    votingData = VotingData(10, 5, null),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votingData = VotingData(1, 50, null),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votingData = VotingData(1, 0, null),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )

                HorizontalVotingBar(
                    votingData = VotingData(0, 0, null),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
            }
        }
    }
}
