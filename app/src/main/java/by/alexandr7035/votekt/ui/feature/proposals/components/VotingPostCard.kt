package by.alexandr7035.votekt.ui.feature.proposals.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.BlockchainActionStatus
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.model.proposal.Proposal
import by.alexandr7035.votekt.ui.components.debug.debugPlaceholder
import by.alexandr7035.votekt.ui.components.preview.ProposalPreviewProvider
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.votingui.HorizontalVotingBar
import by.alexandr7035.votekt.ui.components.web3.ExplorableText
import by.alexandr7035.votekt.ui.feature.proposals.model.ProposalStatusUi
import by.alexandr7035.votekt.ui.feature.proposals.model.getStatusUi
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.utils.AvatarHelper
import by.alexandr7035.votekt.ui.utils.DateFormatters
import by.alexandr7035.votekt.ui.utils.prettifyAddress
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val TIMER_TICK_DELAY = 200L

@Composable
fun VotingPostCard(
    proposal: Proposal,
    isExpanded: Boolean = false,
    showVotingBar: Boolean = true,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(Dimensions.defaultCardCorners)
            )
            .padding(
                vertical = Dimensions.cardPaddingVertical,
                horizontal = Dimensions.cardPaddingHorizontal
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (proposal is Proposal.Deployed) {
            Text(
                text = "#${proposal.proposalNumber}"
            )
        }

        Text(
            text = proposal.title,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        CreatorComponent(
            address = proposal.creatorAddress.hex,
            isSelf = proposal.isSelfCreated,
            onExplorerClick = onExplorerClick,
        )

        PostContentComponent(proposal, isExpanded)

        if (showVotingBar) {
            VotingBar(proposal = proposal)
        }

        PostFooterComponent(proposal, isExpanded)
    }
}

@Composable
private fun PostContentComponent(proposal: Proposal, isExpanded: Boolean) {
    Text(
        text = proposal.description,
        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
        overflow = TextOverflow.Ellipsis,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    )
}

@Composable
private fun PostFooterComponent(
    proposal: Proposal,
    isExpanded: Boolean
) {
    val uiStatus = remember(proposal) {
        proposal.getStatusUi()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProposalDurationComponent(proposal)

        Spacer(modifier = Modifier.weight(1f))

        if (proposal is Proposal.Draft &&
            proposal.deployStatus is BlockchainActionStatus.NotCompleted.Failed &&
            !isExpanded
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.ic_status_rejected),
                contentDescription = stringResource(R.string.proposal_is_failed_to_deploy)
            )
            Spacer(Modifier.width(12.dp))
        }

        ProposalStatusMark(uiStatus)
    }
}

@Composable
private fun ProposalDurationComponent(proposal: Proposal) {
    val context = LocalContext.current

    when (proposal) {
        is Proposal.Deployed -> {
            if (proposal.isFinished.not()) {
                RemainingTimeText(
                    time = proposal.expirationTime,
                )
            } else {
                TimeText(
                    text = stringResource(id = R.string.finished),
                    showAccent = false,
                )
            }
        }

        is Proposal.Draft -> {
            TimeText(
                text = stringResource(
                    id = R.string.proposal_duration_template,
                    DateFormatters.formatDurationTime(
                        duration = proposal.duration,
                        context = context,
                    )
                ),
                showAccent = false
            )
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
                width = 1.dp,
                color = proposalStatusUi.color,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            )
    ) {
        Text(
            text = stringResource(id = proposalStatusUi.title),
            style = TextStyle(
                color = proposalStatusUi.color,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun CreatorComponent(
    address: String,
    imageSize: Dp = 24.dp,
    isSelf: Boolean = true,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val sample = AvatarHelper.getAvatarUrl(identifier = address)
        val imageReq = ImageRequest.Builder(
            LocalContext.current
        ).data(sample).decoderFactory(SvgDecoder.Factory()).crossfade(true).build()

        Text(
            text = stringResource(R.string.creator),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
            )
        )

        AsyncImage(
            model = imageReq,
            contentDescription = stringResource(R.string.cd_user_avatar),
            contentScale = ContentScale.FillHeight,

            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape),
            placeholder = debugPlaceholder(debugPreview = R.drawable.sample_avatar)
        )

        ExplorableText(
            text = if (isSelf) {
                "${address.prettifyAddress()} (You)"
            } else {
                address.prettifyAddress()
            },
            onClick = {
                onExplorerClick(address, ExploreType.ADDRESS)
            },
            fontSize = 14.sp,
        )
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
private fun RemainingTimeText(time: Long) {
    val context = LocalContext.current
    var timeLeft by remember { mutableStateOf(time) }

    val showAccent = remember {
        derivedStateOf {
            calculateRemainingTime(timeLeft) < 1.hours
        }
    }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(TIMER_TICK_DELAY)
            timeLeft--
        }
    }

    TimeText(
        text = stringResource(
            id = R.string.proposal_time_left_template,
            DateFormatters.formatRemainingTime(timeLeft, context)
        ),
        showAccent = showAccent.value,
    )
}

private fun calculateRemainingTime(timeLeft: Long): Duration =
    (timeLeft.toDuration(DurationUnit.MILLISECONDS) - System.currentTimeMillis().toDuration(DurationUnit.MILLISECONDS))

@Composable
private fun TimeText(
    text: String,
    showAccent: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = if (showAccent) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Gray
                }
            )
        )

        Text(
            text = text,
            color = if (showAccent) MaterialTheme.colorScheme.error else Color.Gray,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun VotingBar(proposal: Proposal) {
    if (proposal is Proposal.Deployed) {
        if (proposal.hasVotes()) {
            HorizontalVotingBar(
                votingData = proposal.votingData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                corners = 4.dp
            )
        } else {
            // TODO
        }
    }
}

@Preview
@Composable
private fun VotingPostCard_Preview(
    @PreviewParameter(ProposalPreviewProvider::class) proposal: Proposal
) {
    ScreenPreview {
        VotingPostCard(proposal = proposal)
    }
}
