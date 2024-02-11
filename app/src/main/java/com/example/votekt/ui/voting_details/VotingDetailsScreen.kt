package com.example.votekt.ui.voting_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.domain.votings.VoteType
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.components.voting_bar.HorizontalVotingBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.uiError
import com.example.votekt.ui.utils.mock
import com.example.votekt.ui.utils.prettifyAddress
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VotingDetailsScreen(
    proposalId: Long,
    onBack: () -> Unit = {},
    onShowSnackBar: (message: String, snackBarMode: SnackBarMode) -> Unit = { _, _ -> },
    viewModel: VotingDetailsViewModel = koinViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                title = "Proposal #${proposalId}",
                onBack = {
                    onBack.invoke()
                }
            )
        }) { pv ->

        val screenState = viewModel.screenUi.collectAsStateWithLifecycle().value

        // FIXME collect state from data
        LaunchedEffect(Unit) {
            while (isActive) {
                viewModel.loadProposalById(proposalId)
                delay(10_000)
            }
        }

        when {
            screenState.isProposalLoading -> FullscreenProgressBar(backgroundColor = Color.Transparent)
            screenState.proposal != null -> {
                VotingDetailsScreen_Ui(
                    proposal = screenState.proposal,
                    pv = pv,
                    onVote = { vote ->
                        viewModel.makeVote(proposalId, vote)
                    }
                )
            }

            screenState.error != null -> {
                ErrorFullScreen(
                    // FIXME ui state
                    error = screenState.error.errorType.uiError,
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
}


@Composable
private fun VotingDetailsScreen_Ui(
    proposal: Proposal,
    pv: PaddingValues,
    onVote: (VoteType) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = pv.calculateTopPadding() + 16.dp),
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = proposal.title,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalVotingBar(
                votingData = proposal.votingData,
                modifier = Modifier.height(8.dp),
                corners = 4.dp
            )

            Text(
                text = proposal.description,
                style = MaterialTheme.typography.bodyLarge
            )

            // TODO timer
            Text(text = "Expires at: ${proposal.expirationTime.getFormattedDate("dd MMM yyyy / HH:mm:ss")} UTC")

            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                VotingActionsPanel(
                    votingData = proposal.votingData,
                    modifier = Modifier.wrapContentHeight(),
                    onVote = onVote
                ) {
                    Spacer(Modifier.width(20.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}


@Preview(widthDp = 360, heightDp = 720)
@Composable
fun VotingDetailsScreen_Preview() {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            VotingDetailsScreen_Ui(
                proposal = Proposal.mock(),
                pv = PaddingValues(16.dp),
                onVote = {}
            )
        }
    }
}