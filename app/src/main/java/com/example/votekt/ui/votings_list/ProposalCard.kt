package com.example.votekt.ui.votings_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.components.preview.ProposalPreviewProvider
import com.example.votekt.ui.components.voting_bar.HorizontalVotingBar
import com.example.votekt.ui.theme.VoteKtTheme

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
            onClick.invoke(proposal.uuid)
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

            if (proposal is Proposal.Deployed) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "#${proposal.proposalNumber}",
                    )

                    Text(
                        text = proposal.expirationTime.getFormattedDate("dd MMM yyyy"),
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = proposal.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (proposal) {
                is Proposal.Deployed -> {
                    HorizontalVotingBar(
                        votingData = proposal.votingData,
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                    )
                }

                is Proposal.Draft -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Draft",
                        style = TextStyle(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview
fun ProposalCard_Preview(
    @PreviewParameter(ProposalPreviewProvider::class) proposal: Proposal
) {
    VoteKtTheme {
        ProposalCard(
            proposal = proposal
        )
    }
}