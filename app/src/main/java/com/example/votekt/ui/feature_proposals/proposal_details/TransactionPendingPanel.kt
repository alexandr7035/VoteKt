package com.example.votekt.ui.feature_proposals.proposal_details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.domain.transactions.TransactionStatus
import com.example.votekt.ui.components.preview.TransactionStatusPreviewProvider
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getTransactionStatusUi

@Composable
fun TransactionPendingPanel(
    title: String,
    transactionStatus: TransactionStatus
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
            val statusUi = remember (transactionStatus) {
                transactionStatus.getTransactionStatusUi()
            }

            StatusIcon(icon = statusUi.iconRes)
            Text(text = statusUi.statusExplained)
        }
    }
}

@Composable
private fun StatusIcon(@DrawableRes icon: Int) {
    Image(
        modifier = Modifier.size(32.dp),
        painter = painterResource(id = icon),
        contentDescription = null
    )
}

@Preview
@Composable
fun SelfVoteStatusPanel_Preview(
    @PreviewParameter(TransactionStatusPreviewProvider::class) status: TransactionStatus
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            TransactionPendingPanel(
                title = "Your vote",
                 transactionStatus = status
            )
        }
    }
}
