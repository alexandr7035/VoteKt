package com.example.votekt.ui.votings_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.components.voting_bar.HorizontalVotingBar
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.mock

@Composable
fun ProposalCard(
    proposal: Proposal,
    onClick: (proposalId: Long) -> Unit = {}
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
        ),

    ) {
        Column(
            Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 12.dp),
        ) {
            // TODO date
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = proposal.expirationTime.getFormattedDate("dd MMM yyyy"),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = proposal.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalVotingBar(
                votingData = proposal.votingData,
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Expires at: " + proposal.expirationTime.getFormattedDate("dd MMM yyyy"),
                style = TextStyle(
                    textAlign = TextAlign.End
                )
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