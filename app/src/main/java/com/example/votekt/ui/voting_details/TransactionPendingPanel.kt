package com.example.votekt.ui.voting_details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.components.DotsProgressIndicator
import com.example.votekt.ui.components.preview.TransactionStatusPreviewProvider
import com.example.votekt.ui.theme.VoteKtTheme

enum class TransactionStatusUi {
    CURRENTLY_AWAITED,
    PENDING,
    CONFIRMED,
    FAILED,
}

@Composable
fun TransactionPendingPanel(
    title: String,
    statusUi: TransactionStatusUi
) {
    Column(
        modifier = Modifier
            .background(
                Color.White,
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp
                )
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Black
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (statusUi) {
                TransactionStatusUi.CURRENTLY_AWAITED -> {
                    DotsProgressIndicator(
                        circleSize = 16.dp,
                        travelDistance = 4.dp,
                        spaceBetween = 4.dp
                    )

                    Text(
                        text = "Please, wait"
                    )
                }

                TransactionStatusUi.PENDING -> {
                    StatusIcon(icon = R.drawable.ic_status_pending)

                    Text(
                        text = "Your transaction is pending on blockchain"
                    )
                }

                TransactionStatusUi.CONFIRMED -> {
                    StatusIcon(icon = R.drawable.ic_status_accepted)

                    Text(
                        text = "Your transaction is pending on blockchain"
                    )
                }

                TransactionStatusUi.FAILED -> TODO()
            }
        }
    }
}

@Composable
private fun StatusIcon(@DrawableRes icon: Int) {
    Icon(
        modifier = Modifier.size(56.dp),
        painter = painterResource(id = icon),
        contentDescription = null
    )
}

@Preview
@Composable
fun SelfVoteStatusPanel_Preview(
    @PreviewParameter(TransactionStatusPreviewProvider::class) status: TransactionStatusUi
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            TransactionPendingPanel(
                title = "Your vote",
                 statusUi = status
            )
        }
    }
}