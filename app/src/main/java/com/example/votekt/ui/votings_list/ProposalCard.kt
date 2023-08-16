package com.example.votekt.ui.votings_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.data.model.Proposal
import com.example.votekt.ui.components.HorizontalVotingBar
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


        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .weight(1f)
            ) {
                Text(text = proposal.title, style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = proposal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalVotingBar(
                    votesFor = proposal.votesFor,
                    votesAgainst = proposal.votesAgainst,
                    modifier = Modifier.height(20.dp).fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

//            Box(
//                modifier = Modifier
//                    .width(100.dp)
//                    .wrapContentHeight(),
//            ) {
//                VotingBarCircle(
//                    params = VotingBarParams(
//                        votesFor = proposal.votesFor,
//                        votesAgainst = proposal.votesAgainst,
//                        segmentWidth = 12.dp
//                    )
//                )
//            }

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