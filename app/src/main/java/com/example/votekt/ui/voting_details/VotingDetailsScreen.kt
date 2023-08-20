package com.example.votekt.ui.voting_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.components.voting_bar.VotingBar
import com.example.votekt.ui.components.voting_bar.VotingBarParams
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.ResultColors
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.prettifyAddress
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingDetailsScreen(
    proposalId: Long,
    onBack: () -> Unit = {},
    onShowSnackBar: (message: String, snackBarMode: SnackBarMode) -> Unit = { _, _ -> },
    viewModel: VotingDetailsViewModel = koinViewModel(),
) {
    val proposalState = viewModel.proposalUi.collectAsStateWithLifecycle().value

    Row(Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                AppBar(
                    title = "Proposal #${proposalId}",
                    onBack = {
                        onBack.invoke()
                    }
                )
            }) { pv ->

            LaunchedEffect(Unit) {
                while (isActive) {
                    viewModel.loadProposalById(proposalId)
                    delay(10_000)
                }
            }

            when {
                proposalState.shouldShowData() -> {
                    // State of vote btn action
                    val voteState = viewModel.voteActionState.collectAsStateWithLifecycle().value

                    VotingDetailsScreen_Ui(
                        proposal = proposalState.data!!,
                        pv = pv,
                        onVote = { vote ->
                            viewModel.makeVote(proposalId, vote)
                        }
                    )

                    EventEffect(
                        event = voteState.voteTxSubmittedEvent, onConsumed = viewModel::onVoteCreatedEvent
                    ) { eventData ->
                        if (eventData.isTransactionSubmitted) {
                            onShowSnackBar.invoke(
                                "Vote submitted! Wait for the transaction result\n\nHash: ${eventData.transactionHash!!.prettifyAddress()}",
                                SnackBarMode.Neutral
                            )
                        } else {
                            onShowSnackBar.invoke(
                                "Failed to submit your vote\n\n${eventData.error?.defaultMessage?.title}. ${eventData.error?.defaultMessage?.message}",
                                SnackBarMode.Negative
                            )
                        }
                    }

                    if (voteState.isLoading) {
                        FullscreenProgressBar()
                    }
                }

                proposalState.shouldShowFullError() -> {
                    ErrorFullScreen(
                        appError = proposalState.error!!,
                        onRetry = {
                            viewModel.loadProposalById(proposalId)
                        }
                    )
                }

                proposalState.shouldShowFullLoading() -> {
                    FullscreenProgressBar(backgroundColor = Color.Transparent)
                }
            }
        }
    }
}


@Composable
private fun VotingDetailsScreen_Ui(
    proposal: Proposal,
    pv: PaddingValues,
    onVote: (isFor: Boolean) -> Unit
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

            VotingBar(
                params = VotingBarParams(
                    proposal.votesFor,
                    proposal.votesAgainst,
                    segmentWidth = 20.dp,
                )
            )

            Text(
                text = proposal.description,
                style = MaterialTheme.typography.bodyLarge
            )

            // TODO timer
            Text(text = "Expires at: ${proposal.expirationTime.getFormattedDate("dd MMM yyyy / HH:mm:ss")} UTC")

            Spacer(Modifier.height(16.dp))
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            PrimaryButton(
                text = "Vote against",
                onClick = { onVote.invoke(false) },
                modifier = Modifier.weight(1f),
                buttonColor = ResultColors.negativeColor
            )
            PrimaryButton(
                text = "Vote for",
                onClick = { onVote.invoke(true) },
                modifier = Modifier.weight(1f)
            )
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