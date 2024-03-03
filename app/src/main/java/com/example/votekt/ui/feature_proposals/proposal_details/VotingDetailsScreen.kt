package com.example.votekt.ui.feature_proposals.proposal_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.preview.ProposalPreviewProvider
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.core.AppBar
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
            onBack.invoke()
        })
    }) { pv ->

        Column(
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = pv.calculateTopPadding() + 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                VotingPostCard(
                    proposal = proposal,
                    isExpanded = true
                )

                when (proposal) {
                    is Proposal.Deployed -> {

                    }

                    is Proposal.Draft -> {
                        if (proposal.shouldDeploy) {

                            if (proposal.deployFailed) {
                                Text(
                                    text = "Deploy failed", style = TextStyle(
                                        color = MaterialTheme.colorScheme.error
                                    )
                                )
                            }

                            PrimaryButton(modifier = Modifier.fillMaxWidth(), text = "Deploy proposal", onClick = {

                            })
                        } else {
                            Text(
                                text = "Proposal on deploy"
                            )
                        }
                    }
                }
            }

            if (proposal is Proposal.Draft) {
                if (proposal.isDeployPending()) {
                    proposal.deploymentTransaction?.let {
                        TransactionPendingPanel(
                            title = "Proposal deploy status",
                            transactionStatus = it.status
                        )
                    }
                }
            }
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
            VotingDetailsScreen_Ui(proposal = proposal, onVote = {})
        }
    }
}