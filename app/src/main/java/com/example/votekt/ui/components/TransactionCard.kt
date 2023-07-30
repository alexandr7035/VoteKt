package com.example.votekt.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.tx_history.TransactionsViewModel
import com.example.votekt.ui.utils.prettifyAddress
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionCard(
    transaction: Transaction, viewModel: TransactionsViewModel
) {
    // Set cached status as first value
    val txStatus = remember { mutableStateOf(transaction.status) }

    LaunchedEffect(transaction.hash) {
        if (transaction.status == TxStatus.PENDING) {
            val flow = viewModel.observeTransactionStatus(transaction.hash)
            // Collect the latest transaction status and update the state
            flow.collect { latestStatus ->
                txStatus.value = latestStatus
            }
        }
    }

    TransactionCardUi(
        transaction = transaction,
        transactionStatus = txStatus.value
    )
}

@Composable
private fun TransactionCardUi(
    transaction: Transaction, transactionStatus: TxStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = transaction.type.uiMessage, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)
                )

                val statusUi = getStatusUi(transactionStatus)

                Text(
                    text = statusUi.first, fontSize = 18.sp, color = statusUi.second, fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = transaction.hash.prettifyAddress(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                )


                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = getFormattedDate(transaction.dateSent, "dd MMM yyyy"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Gray
                    )

                    Text(
                        text = "${getFormattedDate(transaction.dateSent, "HH:mm:ss")} UTC",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Gray
                    )
                }

            }
        }

    }
}

@Preview
@Composable
fun TransactionCard_Preview() {
    VoteKtTheme {
        TransactionCardUi(
            transaction = Transaction.mock(), transactionStatus = TxStatus.REVERTED
        )
    }
}

private fun getFormattedDate(timestamp: Long, format: String): String {
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(timestamp)
}

private fun getStatusUi(txStatus: TxStatus): Pair<String, Color> {
    return when (txStatus) {
        TxStatus.PENDING -> Pair("Pending", Color.DarkGray)
        TxStatus.MINED -> Pair("Completed", Color.Green)
        TxStatus.REVERTED -> Pair("Failed", Color.Red)
    }
}