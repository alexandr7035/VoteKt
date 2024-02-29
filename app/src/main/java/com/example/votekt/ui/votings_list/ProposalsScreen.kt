package com.example.votekt.ui.votings_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.VoteKtTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProposalsScreen(
    onProposalClick: (proposalId: String) -> Unit = {},
    onNewProposalClick: () -> Unit = {},
    viewModel: ProposalsViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            AppBar(title = "Proposals")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNewProposalClick.invoke() },
                icon = {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = "Favorite"
                    )
                },
                text = { Text("Create") }
            )
        }) { pv ->

        val state = viewModel.state.collectAsStateWithLifecycle().value

        when {
            state.isLoading -> {
                FullscreenProgressBar(backgroundColor = Color.Transparent)
            }

            state.error != null -> {
                ErrorFullScreen(
                    error = state.error,
                    onRetry = {
                        // TODO
                    }
                )
            }

            else -> {
                if (state.proposals.isNotEmpty()) {
                    ProposalsList(
                        proposals = state.proposals,
                        pv = pv,
                        onProposalClick = onProposalClick,
                    )
                } else {
                    NoProposalsStub(pv = pv)
                }
            }
        }
    }
}

@Composable
private fun ProposalsList(
    proposals: List<Proposal>,
    pv: PaddingValues,
    onProposalClick: (proposalId: String) -> Unit,
) {
    LazyColumn(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(top = pv.calculateTopPadding(), start = 12.dp, end = 12.dp, bottom = pv.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            item {
                Spacer(Modifier.height(8.dp))
            }

            itemsIndexed(
                items = proposals,
                key = { _, proposal ->
                    proposal.uuid
                }
            )  { _, proposal ->
                ProposalCard(
                    proposal = proposal,
                    onClick = { onProposalClick(proposal.uuid) }
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
            }
        })
}


@Composable
private fun NoProposalsStub(pv: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = pv.calculateTopPadding(), bottom = pv.calculateBottomPadding()
            ), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No Proposals yet", style = MaterialTheme.typography.titleLarge)
    }
}

// TODO
@Composable
@Preview()
fun ProposalsScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
//        ProposalsList(proposals = List(5) { Proposal.mock() }, pv = PaddingValues(12.dp), onProposalClick = {})
    }
}