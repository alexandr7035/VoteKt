package com.example.votekt.ui.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus
import com.example.votekt.ui.tx_history.TransactionsViewModel
import com.example.votekt.ui.utils.prettifyAddress

@Composable
fun TransactionCard(
    transaction: Transaction,
    viewModel: TransactionsViewModel
) {

    val colors = listOf(Color(0xFFffe53b), Color(0xFFff2525))

    // Create a mutable state to hold the transaction status
    val txStatus = remember { mutableStateOf(TxStatus.PENDING) }

    // Use LaunchedEffect to collect the transaction status only once
    LaunchedEffect(transaction.hash) {
        val flow = viewModel.observeTransactionStatus(transaction.hash)
        // Collect the latest transaction status and update the state
        flow.collect { latestStatus ->
            txStatus.value = latestStatus
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 4.dp,
                brush = Brush.horizontalGradient(colors = colors),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Hash ${transaction.hash.prettifyAddress()}")
        Text(text = transaction.dateSent.toString())
        Text(text = txStatus.value.toString())
    }
}