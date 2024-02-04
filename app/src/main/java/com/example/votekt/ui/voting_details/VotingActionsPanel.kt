package com.example.votekt.ui.voting_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getVoteColor
import com.example.votekt.ui.utils.showToast

@Composable
fun VotingActionsPanel(
    votingData: VotingData,
    selfVote: VoteType? = null,
    onVote: (VoteType) -> Unit = {},
    modifier: Modifier,
    centerContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        val votingEnabled = selfVote == null

        VoteButton(
            onClick = { onVote(VoteType.VOTE_AGAINST) },
            votesCount = votingData.votesAgainst,
            tint = getVoteColor(false),
            enabled = votingEnabled,
            selected = selfVote == VoteType.VOTE_AGAINST
        )

        centerContent()

        VoteButton(
            onClick = { onVote(VoteType.VOTE_FOR) },
            votesCount = votingData.votesFor,
            tint = getVoteColor(true),
            enabled = votingEnabled,
            selected = selfVote == VoteType.VOTE_FOR
        )
    }
}

@Composable
private fun VoteButton(
    onClick: () -> Unit,
    votesCount: Int,
    corners: Dp = 4.dp,
    tint: Color,
    size: Dp = 32.dp,
    selected: Boolean = false,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    val clickableModifier = if (enabled) {
        Modifier.clickable(
            onClick = onClick, indication = rememberRipple(radius = size, bounded = false), interactionSource = interactionSource
        )
    } else {
        Modifier.clickable {
            context.showToast("You can't vote")
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = clickableModifier.then(
                Modifier
                    .background(
                        shape = RoundedCornerShape(corners),
                        color = if (selected) tint.copy(alpha = 0.3f) else Color.Transparent,
                    )
                    .padding(size / 4)
            )
        ) {
            Icon(
                modifier = Modifier.size(size), painter = painterResource(id = R.drawable.thumb_up), contentDescription = null, tint = tint
            )
        }

        Text(
            text = "$votesCount", style = TextStyle(
                fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
@Preview
private fun VotingActionsPanel_Preview() {
    VoteKtTheme {
        Column(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VotingActionsPanel(
                votingData = VotingData(1, 2),
                modifier = Modifier.wrapContentWidth(),
                centerContent = {
                    Text(text = "Centet content", modifier = Modifier.padding(horizontal = 40.dp))
                }
            )

            VotingActionsPanel(
                selfVote = VoteType.VOTE_AGAINST,
                votingData = VotingData(1, 2),
                modifier = Modifier.wrapContentWidth(),
                centerContent = {
                    Text(text = "Centet content", modifier = Modifier.padding(horizontal = 40.dp))
                }
            )
        }
    }
}