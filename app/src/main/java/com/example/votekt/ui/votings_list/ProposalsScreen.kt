package com.example.votekt.ui.votings_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.components.ProposalCard
import com.example.votekt.ui.theme.VoteKtTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalsScreen(
    onProposalClick: (proposalId: String) -> Unit = {},
    viewModel: ProposalsViewModel = koinViewModel()
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Proposals") })
    }) { pv ->

        val proposals = viewModel.proposalsUi.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadProposals()
        }

        LazyColumn(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = pv.calculateTopPadding(), start = 12.dp, end = 12.dp, bottom = pv.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(proposals.size) {
                    ProposalCard(proposal = proposals[it], onClick = { onProposalClick.invoke(it) })
                }

                item {
                    Spacer(Modifier.height(12.dp))
                }
            })
    }
}

@Composable
@Preview(widthDp = 360)
fun ProposalsScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
        ProposalsScreen()
    }
}