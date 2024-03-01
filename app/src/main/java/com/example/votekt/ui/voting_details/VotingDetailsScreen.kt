package com.example.votekt.ui.voting_details

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.space.ui.components.debug.debugPlaceholder
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.votekt.R
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.preview.ProposalPreviewProvider
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.components.voting_bar.HorizontalVotingBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.AvatarHelper
import com.example.votekt.ui.utils.prettifyAddress
import com.example.votekt.ui.voting_details.model.ProposalStatusUi
import com.example.votekt.ui.voting_details.model.getStatusUi
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VotingDetailsScreen(
    proposalId: String,
    onBack: () -> Unit = {},
    onShowSnackBar: (message: String, snackBarMode: SnackBarMode) -> Unit = { _, _ -> },
    viewModel: VotingDetailsViewModel = koinViewModel(),
) {

    LaunchedEffect(proposalId) {
        viewModel.loadProposalById(proposalId)
    }

    val screenState = viewModel.state.collectAsStateWithLifecycle().value

    when {
        screenState.isProposalLoading -> FullscreenProgressBar(backgroundColor = Color.Transparent)

        screenState.proposal != null -> {
            VotingDetailsScreen_Ui(
                proposal = screenState.proposal,
                onVote = { vote ->
                    // TODO non local id
//                        viewModel.makeVote(proposalId, vote)
                }
            )
        }

        screenState.error != null -> {
            ErrorFullScreen(
                error = screenState.error,
                onRetry = {
                    viewModel.loadProposalById(proposalId)
                }
            )
        }
    }

    EventEffect(
        event = screenState.selfVoteSubmittedEvent, onConsumed = viewModel::onVoteSubmittedEvent
    ) { transactionHash ->
        onShowSnackBar.invoke(
            "Vote submitted! Wait for the transaction result\nHash: ${transactionHash.value.prettifyAddress()}",
            SnackBarMode.Neutral
        )
    }
}


@Composable
private fun VotingDetailsScreen_Ui(
    proposal: Proposal,
    onVote: (VoteType) -> Unit,
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = when (proposal) {
                    is Proposal.Draft -> {
                        stringResource(R.string.draft_proposal)
                    }

                    is Proposal.Deployed -> {
                        stringResource(id = R.string.proposal_title_template, proposal.proposalNumber)
                    }
                },
                onBack = {
                    onBack.invoke()
                }
            )
        }) { pv ->

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = pv.calculateTopPadding() + 16.dp),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                VotingPostCard(
                    proposal = proposal
                )

                when (proposal) {
                    is Proposal.Deployed -> {

                    }

                    is Proposal.Draft -> {
                        if (proposal.shouldDeploy) {

                            if (proposal.deployFailed) {
                                Text(
                                    text = "Deploy failed",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.error
                                    )
                                )
                            }

                            PrimaryButton(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Deploy proposal",
                                onClick = {

                                }
                            )
                        } else {
                            Text(
                                text = "Proposal on deploy"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VotingPostCard(proposal: Proposal) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(
                vertical = 12.dp,
                horizontal = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = proposal.title,
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Creator(address = proposal.creatorAddress.value)

        val descriptionExpanded = remember {
            mutableStateOf(false)
        }

        Text(
            text = proposal.description,
            maxLines = if (descriptionExpanded.value) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.clickable {
                descriptionExpanded.value = !descriptionExpanded.value
            }
        )

        VotingBar(proposal = proposal)

        val uiStatus = remember(proposal) {
            proposal.getStatusUi()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (proposal is Proposal.Deployed) {
                Text(
                    text = "#${proposal.proposalNumber}"
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
            text = proposalStatusUi.title,
            style = TextStyle(
                color = proposalStatusUi.color,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun Creator(
    address: String,
    imageSize: Dp = 24.dp,
    isSelf: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val sample = AvatarHelper.getAvatarUrl(identifier = address)
        val imageReq = ImageRequest.Builder(LocalContext.current)
            .data(sample)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true).build()

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

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun VotingDetailsScreen_Preview(
    @PreviewParameter(ProposalPreviewProvider::class) proposal: Proposal
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            VotingDetailsScreen_Ui(
                proposal = proposal,
                onVote = {}
            )
        }
    }
}