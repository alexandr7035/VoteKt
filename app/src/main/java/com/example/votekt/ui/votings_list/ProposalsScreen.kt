package com.example.votekt.ui.votings_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.components.ProposalCard
import com.example.votekt.ui.theme.VoteKtTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalsScreen() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Proposals") })
    }) { pv ->
        LazyColumn(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = pv.calculateTopPadding(), start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(7) {
                    ProposalCard(proposal = Proposal.mock())
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