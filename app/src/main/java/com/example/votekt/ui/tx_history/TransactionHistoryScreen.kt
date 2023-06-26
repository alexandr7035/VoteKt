package com.example.votekt.ui.tx_history

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionHistoryScreen(viewModel: TransactionsViewModel = koinViewModel()) {
    Text(text="txs")

    LaunchedEffect(Unit) {
        viewModel.load()
    }
}