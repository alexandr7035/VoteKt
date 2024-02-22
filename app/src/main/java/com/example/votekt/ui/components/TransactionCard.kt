package com.example.votekt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.core.crypto.BalanceFormatter
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.ui.components.preview.TransactionPreviewProvider
import com.example.votekt.ui.components.preview.TransactionStatusPreviewProvider
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.mock
import com.example.votekt.ui.utils.prettifyAddress
import com.example.votekt.ui.voting_details.TransactionStatusUi

@Composable
fun TransactionCard(
    transaction: Transaction,
) {
    TransactionCardUi(
        transaction = transaction,
    )
}

@Composable
private fun TransactionCardUi(
    transaction: Transaction
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusUi = remember(transaction.status) {
                    getTxStatusMark(transaction.status)
                }

                Image(
                    painter = painterResource(id = statusUi.second),
                    contentDescription = "Transaction status ${statusUi.first}",
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = transaction.type.uiMessage,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )

                Text(
                    text = transaction.hash.prettifyAddress(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            transaction.gasFee?.let {
                Text(
                    text = "- ${
                        BalanceFormatter.formatAmountWithSymbol(
                            amount = it.toEther(),
                            symbol = "ETH"
                        )
                    }",
                    style = TextStyle(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = transaction.dateSent.getFormattedDate("dd MMM yyyy HH:mm:ss"),
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ),
            )
        }
    }
}

@Preview
@Composable
fun TransactionCard_Preview(
    @PreviewParameter(TransactionPreviewProvider::class) transaction: Transaction
) {
    VoteKtTheme {
        TransactionCardUi(
            transaction = transaction
        )
    }
}

// TODO move to utils
private fun getTxStatusMark(status: TxStatus): Pair<String, Int> {
    return when (status) {
        TxStatus.PENDING -> "Pending" to R.drawable.ic_status_pending
        TxStatus.MINED -> "Completed" to R.drawable.ic_status_accepted
        TxStatus.REVERTED -> "Failed" to R.drawable.ic_status_rejected
    }
}
