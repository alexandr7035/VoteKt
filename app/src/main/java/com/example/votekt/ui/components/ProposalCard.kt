package com.example.votekt.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.theme.VoteKtTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalCard(
    proposal: Proposal,
    onClick: (proposalId: String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            onClick.invoke(proposal.id)
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 6.dp,
        )
    ) {

        Column(Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
            Text(text = proposal.title, style = MaterialTheme.typography.headlineMedium)
            Text(text = proposal.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalVotingBar(
                params = VotingBarParams(
                    votesFor = 10,
                    votesAgainst = 5,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }

    }
}

@Composable
@Preview
fun ProposalCard_Preview() {
    VoteKtTheme {
        ProposalCard(
            proposal = Proposal.mock()
        )
    }
}