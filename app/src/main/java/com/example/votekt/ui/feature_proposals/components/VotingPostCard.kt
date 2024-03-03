package com.example.votekt.ui.feature_proposals.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.alexandr7035.space.ui.components.debug.debugPlaceholder
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.votekt.R
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.components.voting_bar.HorizontalVotingBar
import com.example.votekt.ui.utils.AvatarHelper
import com.example.votekt.ui.utils.DateFormatters
import com.example.votekt.ui.utils.prettifyAddress
import com.example.votekt.ui.feature_proposals.model.ProposalStatusUi
import com.example.votekt.ui.feature_proposals.model.getStatusUi
import kotlinx.coroutines.delay

@Composable
fun VotingPostCard(
    proposal: Proposal,
    isExpanded: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White, shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 12.dp, horizontal = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (proposal is Proposal.Deployed) {
            Text(
                text = "#${proposal.proposalNumber}"
            )
        }

        Text(
            text = proposal.title, style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Creator(address = proposal.creatorAddress.value)

        Text(
            text = proposal.description,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        )

        VotingBar(proposal = proposal)

        val uiStatus = remember(proposal) {
            proposal.getStatusUi()
        }

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            if (proposal is Proposal.Deployed) {
                RemainingTimeText(
                    time = proposal.expirationTime,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ProposalStatusMark(uiStatus)
        }
    }
}

@Composable
private fun ProposalStatusMark(
    proposalStatusUi: ProposalStatusUi
) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .border(
                width = 1.dp, color = proposalStatusUi.color, shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 8.dp, horizontal = 12.dp
            )
    ) {
        Text(
            text = proposalStatusUi.title, style = TextStyle(
                color = proposalStatusUi.color, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun Creator(
    address: String, imageSize: Dp = 24.dp, isSelf: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val sample = AvatarHelper.getAvatarUrl(identifier = address)
        val imageReq = ImageRequest.Builder(LocalContext.current).data(sample).decoderFactory(SvgDecoder.Factory()).crossfade(true).build()

        Text(
            text = stringResource(R.string.creator), style = TextStyle(
                fontWeight = FontWeight.Bold,
            )
        )

        AsyncImage(
            model = imageReq, contentDescription = stringResource(R.string.cd_user_avatar), contentScale = ContentScale.FillHeight,

            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape), placeholder = debugPlaceholder(debugPreview = R.drawable.sample_avatar)
        )

        Text(
            text = if (isSelf) {
                // TODO contract update
                "${address.prettifyAddress()} (You)"
            } else {
                address.prettifyAddress()
            }
        )
    }
}

@Composable
private fun RemainingTimeText(time: Long) {
    val context = LocalContext.current
    var timeLeft by remember { mutableStateOf(time) }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(200L)
            timeLeft--
        }
    }

    Text(
        text = stringResource(
            id = R.string.proposal_time_left_template,
            DateFormatters.formatRemainingTime(timeLeft, context)
        )
    )
}

@Composable
private fun VotingBar(proposal: Proposal) {
    if (proposal is Proposal.Deployed) {
        if (proposal.hasVotes()) {
            HorizontalVotingBar(
                votingData = proposal.votingData, modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp), corners = 4.dp
            )
        } else {
            // TODO
        }
    }
}