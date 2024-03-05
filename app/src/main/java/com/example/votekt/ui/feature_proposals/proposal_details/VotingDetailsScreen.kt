package com.example.votekt.ui.feature_proposals.proposal_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.preview.ProposalPreviewProvider
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.feature_proposals.components.TransactionStatusCard
import com.example.votekt.ui.feature_proposals.components.VotingPostCard
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.prettifyAddress
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
                onBack = onBack,
                proposal = screenState.proposal,
                onVote = { vote ->
                    // TODO non local id
//                        viewModel.makeVote(proposalId, vote)
                })
        }

        screenState.error != null -> {
            ErrorFullScreen(error = screenState.error, onRetry = {
                viewModel.loadProposalById(proposalId)
            })
        }
    }

    EventEffect(
        event = screenState.selfVoteSubmittedEvent, onConsumed = viewModel::onVoteSubmittedEvent
    ) { transactionHash ->
        // TODO composition local
        onShowSnackBar.invoke(
            "Vote submitted! Wait for the transaction result\nHash: ${transactionHash.value.prettifyAddress()}", SnackBarMode.Neutral
        )
    }
}


@Composable
private fun VotingDetailsScreen_Ui(
    proposal: Proposal,
    onVote: (VoteType) -> Unit,
    onBack: () -> Unit = {}
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        AppBar(title = when (proposal) {
            is Proposal.Draft -> {
                stringResource(R.string.draft_proposal)
            }

            is Proposal.Deployed -> {
                stringResource(id = R.string.proposal_title_template, proposal.proposalNumber)
            }
        }, onBack = {
            onBack()
        })
    }) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    // TODO dimens
                    start = 16.dp,
                    end = 16.dp,
                    top = pv.calculateTopPadding() + 16.dp,
                    bottom = pv.calculateTopPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                VotingPostCard(
                    proposal = proposal,
                    isExpanded = true
                )
            }

            // Consider adding activity history later
            when (proposal) {
                is Proposal.Draft -> {
                    if (proposal.shouldBeDeployed || proposal.isDeployPending) {
                        DeployStatusPanel(proposal)
                    }
                }

                is Proposal.Deployed -> {

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
fun DeployStatusPanel(proposal: Proposal.Draft) {
    ProposalActionCard(
        title = if (proposal.isDeployPending) {
            stringResource(R.string.deploy_failed)
        } else {
            stringResource(R.string.proposal_is_not_deployed)
        }
    ) {
        when {
            proposal.isDeployPending -> {
                proposal.deploymentTransaction?.let {
                    TransactionStatusCard(transactionStatus = it.status)
                }
            }

            else -> {
                if (proposal.isDeployFailed) {
                    proposal.deploymentTransaction?.let {
                        TransactionStatusCard(transactionStatus = it.status)
                    }
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (proposal.isDeployFailed) {
                        stringResource(id = R.string.try_again)
                    } else {
                        stringResource(R.string.deploy)
                    },
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}

@Composable
fun VoteStatusPanel(proposal: Proposal.Deployed) {

}

@Preview(widthDp = 360, heightDp = 720)
@Composable
fun VotingDetailsScreen_Preview(
    @PreviewParameter(ProposalPreviewProvider::class) proposal: Proposal
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            VotingDetailsScreen_Ui(proposal = proposal, onVote = {})
        }
    }
}