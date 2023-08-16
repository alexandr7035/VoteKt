package com.example.votekt.ui.votings_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.VoteKtTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalsScreen(
    onProposalClick: (proposalId: Long) -> Unit = {},
    onNewProposalClick: ()-> Unit = {},
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

        val state = viewModel.proposalsListUi.collectAsStateWithLifecycle().value
        Log.d("TEST", state.toString())

        LaunchedEffect(Unit) {
            viewModel.loadProposals()
        }

        when {
            state.shouldShowData() -> {
                if (state.data!!.isNotEmpty()) {
                    ProposalsList(
                        proposals = state.data, pv = pv, onProposalClick = onProposalClick
                    )
                } else {
                    NoProposalsStub(pv = pv)
                }
            }

            state.shouldShowFullError() -> {
                ErrorFullScreen(appError = state.error!!, onRetry = {
                    viewModel.loadProposals()
                })
            }
        }
    }
}

@Composable
private fun ProposalsList(
    proposals: List<Proposal>, pv: PaddingValues, onProposalClick: (proposalId: Long) -> Unit
) {
    LazyColumn(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(top = pv.calculateTopPadding(), start = 12.dp, end = 12.dp, bottom = pv.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            item {
                Spacer(Modifier.height(8.dp))
            }

            items(proposals.size) {
                val proposal = proposals[it]
                ProposalCard(proposal = proposal, onClick = { onProposalClick.invoke(proposal.id) })
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

@Composable
@Preview()
fun ProposalsScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
        ProposalsList(proposals = List(5) { Proposal.mock() }, pv = PaddingValues(12.dp), onProposalClick = {})
    }
}