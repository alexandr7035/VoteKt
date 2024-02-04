package com.example.votekt.ui.voting_details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getVoteColor

@Composable
fun VotingActionsPanel(
    votesFor: Int,
    votesAgainst: Int,
    modifier: Modifier,
    centerContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.thumb_down),
                contentDescription = null,
                tint = getVoteColor(false)
            )
        }

        Text(
            text = "${votesAgainst}",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        centerContent()

        Text(
            text = "${votesFor}",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.thumb_up),
                contentDescription = null,
                tint = getVoteColor(true)
            )
        }
    }
}

@Composable
@Preview
private fun VotingActionsPanel_Preview() {
    VoteKtTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VotingActionsPanel(
                votesAgainst = 1,
                votesFor = 2,
                modifier = Modifier.wrapContentWidth(),
                centerContent = {
                    Text(text = "Centet content")
                }
            )
        }
    }
}