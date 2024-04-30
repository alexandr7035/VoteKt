package com.example.votekt.ui.feature_proposals.proposal_details

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.ethereum.model.ETHER
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.R
import com.example.votekt.domain.core.BlockchainActionStatus
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.domain.votings.VotingData
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.RoundedButton
import com.example.votekt.ui.components.TipView
import com.example.votekt.ui.components.preview.ProposalPreviewProvider
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature_proposals.components.TransactionStatusCard
import com.example.votekt.ui.feature_proposals.components.VotingBarExtended
import com.example.votekt.ui.feature_proposals.components.VotingPostCard
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.ui.utils.getVoteColor
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VotingDetailsScreen(
    proposalId: String,
    onNavigationEvent: (ProposalDetailsScreenNavigationEvent) -> Unit,
    viewModel: VotingDetailsViewModel = koinViewModel(),
) {
    BackHandler() {
        viewModel.onIntent(ProposalDetailsScreenIntent.GoBack)
    }

    LaunchedEffect(proposalId) {
        viewModel.onIntent(ProposalDetailsScreenIntent.EnterScreen(proposalId))
    }

    val screenState = viewModel.state.collectAsStateWithLifecycle().value

    when {
        screenState.isProposalLoading -> FullscreenProgressBar(backgroundColor = Color.Transparent)

        screenState.proposal != null -> {
            VotingDetailsScreen_Ui(
                proposal = screenState.proposal,
                deploymentFee = screenState.proposalDeploymentFee,
                onIntent = {
                    viewModel.onIntent(it)
                }
            )
        }

        screenState.error != null -> {
            ErrorFullScreen(error = screenState.error, onRetry = {
                viewModel.loadProposalById(proposalId)
            })
        }
    }

    NavigationEventEffect(
        event = screenState.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent,
    ) {
        onNavigationEvent(it)
    }
}


@Composable
private fun VotingDetailsScreen_Ui(
    proposal: Proposal,
    deploymentFee: Wei?,
    onIntent: (ProposalDetailsScreenIntent) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
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
                onIntent(ProposalDetailsScreenIntent.GoBack)
            },
            actions = {
                if (proposal is Proposal.Draft && proposal.deployStatus is BlockchainActionStatus.NotCompleted) {
                    IconButton(onClick = {
                        onIntent(ProposalDetailsScreenIntent.DeleteClick(Uuid(proposal.uuid)))
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_draft),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        )
    }) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = Dimensions.screenPaddingHorizontal,
                    end = Dimensions.screenPaddingHorizontal,
                    top = pv.calculateTopPadding() + Dimensions.screenPaddingVertical,
                    bottom = pv.calculateTopPadding() + Dimensions.screenPaddingVertical,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            if (proposal is Proposal.Draft) {
                deploymentFee?.let {
                    TipView(
                        text = UiText.DynamicString(
                            stringResource(
                                id = R.string.deploy_proposal_explanation_template,
                                BalanceFormatter.formatAmountWithSymbol(
                                    amount = deploymentFee.toEther(),
                                    symbol = "ETH",
                                )
                            ),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Card(
                elevation = CardDefaults.cardElevation(Dimensions.defaultCardElevation),
            ) {
                VotingPostCard(
                    proposal = proposal,
                    isExpanded = true,
                    showVotingBar = false,
                    onExplorerClick = { payload, exploreType ->
                        onIntent(
                            ProposalDetailsScreenIntent.ExplorerUrlClick(
                                payload = payload,
                                exploreType = exploreType
                            )
                        )
                    }
                )
            }

            // Consider adding activity history later
            when (proposal) {
                is Proposal.Draft -> {
                    if (proposal.deployStatus is BlockchainActionStatus.NotCompleted ||
                        proposal.deployStatus is BlockchainActionStatus.Pending
                    ) {
                        DeployStatusPanel(
                            proposal = proposal,
                            onDeployClick = {
                                onIntent(ProposalDetailsScreenIntent.DeployClick(Uuid(proposal.uuid)))
                            },
                        )
                    }
                }

                is Proposal.Deployed -> {
                    VotingBarExtended(votingData = proposal.votingData)

                    if (proposal.isFinished.not()) {
                        VoteStatusPanel(
                            proposal = proposal,
                            onVote = {
                                onIntent(
                                    ProposalDetailsScreenIntent.MakeVoteClick(
                                        proposalNumber = proposal.proposalNumber,
                                        voteType = it
                                    )
                                )
                            }
                        )
                    } else {
                        // TODO voting summary
                    }
                }
            }
        }
    }
}

@Composable
private fun ProposalActionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            content()
        }
    }
}

@Composable
fun DeployStatusPanel(
    proposal: Proposal.Draft,
    onDeployClick: () -> Unit,
) {
    ProposalActionCard(
        title = when (proposal.deployStatus) {
            is BlockchainActionStatus.NotCompleted -> stringResource(R.string.proposal_is_not_deployed)
            is BlockchainActionStatus.Pending -> stringResource(R.string.deploy_in_progress)
            else -> error("unexpected deploy status ${proposal.deployStatus} in this component")
        }
    ) {
        when (proposal.deployStatus) {
            is BlockchainActionStatus.Pending -> {
                proposal.deploymentTransaction?.let {
                    TransactionStatusCard(transactionStatus = it.status)
                }
            }

            is BlockchainActionStatus.NotCompleted -> {
                if (proposal.deployStatus is BlockchainActionStatus.NotCompleted.Failed) {
                    proposal.deploymentTransaction?.let {
                        TransactionStatusCard(transactionStatus = it.status)
                    }
                }

                RoundedButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = when (proposal.deployStatus) {
                        is BlockchainActionStatus.NotCompleted.Failed -> stringResource(id = R.string.try_again)
                        else -> stringResource(R.string.deploy)
                    },
                    onClick = { onDeployClick() }
                )
            }

            else -> error("unexpected deploy status ${proposal.deployStatus} in this component")
        }
    }
}

@Composable
fun VoteStatusPanel(
    proposal: Proposal.Deployed,
    onVote: (VoteType) -> Unit,
) {
    ProposalActionCard(
        title = when (proposal.selfVoteStatus) {
            is BlockchainActionStatus.NotCompleted -> stringResource(R.string.make_your_vote)
            else -> stringResource(R.string.your_vote)
        }
    ) {

        when (proposal.selfVoteStatus) {
            is BlockchainActionStatus.NotCompleted -> {
                if (proposal.selfVoteStatus is BlockchainActionStatus.NotCompleted.Failed) {
                    proposal.voteTransaction?.let {
                        TransactionStatusCard(transactionStatus = it.status)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VotingButton(
                        modifier = Modifier.weight(1f),
                        voteType = VoteType.VOTE_AGAINST,
                        votingData = proposal.votingData,
                        selfVoteStatus = proposal.selfVoteStatus,
                    ) {
                        onVote(it)
                    }

                    VotingButton(
                        modifier = Modifier.weight(1f),
                        voteType = VoteType.VOTE_FOR,
                        votingData = proposal.votingData,
                        selfVoteStatus = proposal.selfVoteStatus,
                    ) { vote ->
                        onVote(vote)
                    }
                }
            }

            BlockchainActionStatus.Completed -> {
                proposal.votingData.selfVote?.let { it ->
                    VotingButton(
                        modifier = Modifier.fillMaxWidth(),
                        voteType = it,
                        votingData = proposal.votingData,
                        selfVoteStatus = proposal.selfVoteStatus,
                    ) { vote ->
                        onVote(vote)
                    }
                }
            }

            BlockchainActionStatus.Pending -> {
                proposal.voteTransaction?.let {
                    TransactionStatusCard(transactionStatus = it.status)
                }
            }
        }
    }
}

@Composable
private fun VotingButton(
    modifier: Modifier = Modifier,
    voteType: VoteType,
    votingData: VotingData,
    selfVoteStatus: BlockchainActionStatus,
    onClick: (VoteType) -> Unit,
) {
    val displayedVotersCount: Int? = when (selfVoteStatus) {
        is BlockchainActionStatus.Completed -> {
            val count = when (voteType) {
                VoteType.VOTE_FOR -> votingData.votesFor - 1
                VoteType.VOTE_AGAINST -> votingData.votesAgainst - 1
            }

            if (count > 1) count else null
        }

        else -> null
    }

    val accentColor = remember(voteType) {
        val alpha = 0.5f

        when (selfVoteStatus) {
            is BlockchainActionStatus.Completed -> {
                when (voteType) {
                    VoteType.VOTE_FOR -> getVoteColor(true).copy(alpha)
                    VoteType.VOTE_AGAINST -> getVoteColor(false).copy(alpha)

                }
            }

            else -> {
                when (voteType) {
                    VoteType.VOTE_FOR -> getVoteColor(true)
                    VoteType.VOTE_AGAINST -> getVoteColor(false)
                }
            }
        }
    }

    val textRes = when (selfVoteStatus) {
        is BlockchainActionStatus.Completed -> {
            when (voteType) {
                VoteType.VOTE_FOR -> R.string.supported
                VoteType.VOTE_AGAINST -> R.string.not_supported
            }
        }

        else -> {
            when (voteType) {
                VoteType.VOTE_FOR -> R.string.support
                VoteType.VOTE_AGAINST -> R.string.reject
            }
        }
    }

    Button(
        modifier = modifier,
        onClick = { onClick(voteType) },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = accentColor,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
        enabled = selfVoteStatus is BlockchainActionStatus.NotCompleted
    ) {
        Image(
            modifier = Modifier.size(16.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            painter = painterResource(
                id = when (voteType) {
                    VoteType.VOTE_AGAINST -> R.drawable.thumb_down
                    VoteType.VOTE_FOR -> R.drawable.thumb_up
                },
            ),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(id = textRes)
        )

        displayedVotersCount?.let { votesCount ->
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = stringResource(id = R.string.proposal_self_votes_count, votesCount))
        }
    }
}

@Preview
@Composable
fun VotingDetailsScreen_Preview(
    @PreviewParameter(ProposalPreviewProvider::class) proposal: Proposal
) {
    ScreenPreview {
        VotingDetailsScreen_Ui(
            proposal = proposal,
            deploymentFee = 0.25.ETHER,
            onIntent = {},
        )
    }
}
