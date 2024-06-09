package by.alexandr7035.votekt.ui.feature.proposals.details

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
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.BlockchainActionStatus
import by.alexandr7035.votekt.domain.core.Uuid
import by.alexandr7035.votekt.domain.votings.Proposal
import by.alexandr7035.votekt.domain.votings.VoteType
import by.alexandr7035.votekt.domain.votings.VotingData
import by.alexandr7035.votekt.ui.components.ErrorFullScreen
import by.alexandr7035.votekt.ui.components.RoundedButton
import by.alexandr7035.votekt.ui.components.TipView
import by.alexandr7035.votekt.ui.components.preview.ProposalPreviewProvider
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.progress.FullscreenProgressBar
import by.alexandr7035.votekt.ui.core.AppBar
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.proposals.components.VotingBarExtended
import by.alexandr7035.votekt.ui.feature.proposals.components.VotingPostCard
import by.alexandr7035.votekt.ui.feature.transactions.components.TransactionStatusCard
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.utils.BalanceFormatter
import by.alexandr7035.votekt.ui.utils.getVoteColor
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

private const val DISABLED_VOTING_BUTTON_ALPHA = 0.5F

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProposalDetailsScreen(
    proposalId: String,
    onNavigationEvent: (ProposalDetailsScreenNavigationEvent) -> Unit,
    viewModel: VotingDetailsViewModel = koinViewModel(),
) {
    BackHandler {
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
        VotingScreenAppBar(proposal, onIntent)
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
                    DeploymentFeeTip(deploymentFee)
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
                    DeployedProposalVotingComponents(proposal, onIntent)
                }
            }
        }
    }
}

@Composable
private fun DeployedProposalVotingComponents(
    proposal: Proposal.Deployed,
    onIntent: (ProposalDetailsScreenIntent) -> Unit
) {
    VotingBarExtended(votingData = proposal.votingData)

    if (proposal.isFinished.not()) {
        SelfVotePanel(
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
    }
}

@Composable
private fun DeploymentFeeTip(deploymentFee: Wei) {
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

@Composable
private fun VotingScreenAppBar(
    proposal: Proposal,
    onIntent: (ProposalDetailsScreenIntent) -> Unit
) {
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
fun SelfVotePanel(
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
    val displayedVotersCount: Int? = getDisplayedVotersCount(selfVoteStatus, voteType, votingData)
    val accentColor = getVotingButtonColor(voteType, selfVoteStatus)
    val textRes = getVotingButtonText(selfVoteStatus, voteType)

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

@Composable
private fun getDisplayedVotersCount(
    selfVoteStatus: BlockchainActionStatus,
    voteType: VoteType,
    votingData: VotingData
) = when (selfVoteStatus) {
    is BlockchainActionStatus.Completed -> {
        val count = when (voteType) {
            VoteType.VOTE_FOR -> votingData.votesFor - 1
            VoteType.VOTE_AGAINST -> votingData.votesAgainst - 1
        }

        if (count > 1) count else null
    }

    else -> null
}

@Composable
private fun getVotingButtonText(
    selfVoteStatus: BlockchainActionStatus,
    voteType: VoteType
) = when (selfVoteStatus) {
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

@Composable
private fun getVotingButtonColor(
    voteType: VoteType,
    selfVoteStatus: BlockchainActionStatus
) = remember(voteType) {
    when (selfVoteStatus) {
        is BlockchainActionStatus.Completed -> {
            when (voteType) {
                VoteType.VOTE_FOR -> getVoteColor(true).copy(DISABLED_VOTING_BUTTON_ALPHA)
                VoteType.VOTE_AGAINST -> getVoteColor(false).copy(DISABLED_VOTING_BUTTON_ALPHA)
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
