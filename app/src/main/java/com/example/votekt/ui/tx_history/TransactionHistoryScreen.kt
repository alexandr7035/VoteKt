package com.example.votekt.ui.tx_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.data.model.Transaction
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.TransactionCard
import com.example.votekt.ui.core.AppBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(viewModel: TransactionsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.loadTransactionList()
    }

    Scaffold(topBar = {
        AppBar(
            title = "Transactions",
            actions = {
                IconButton(onClick = { viewModel.clearTransactions() }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.clear_transactions),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }) { pv ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(top = pv.calculateTopPadding())
        ) {
            when {
                state.shouldShowData() -> {
                    if (state.data!!.isNotEmpty()) {
                        TransactionsList(
                            transactions = state.data,
                            viewModel = viewModel
                        )
                    } else {
                        NoTransactionsStub()
                    }
                }

                state.shouldShowFullError() -> {
                    ErrorFullScreen(appError = state.error!!, onRetry = {
                        viewModel.loadTransactionList()
                    })
                }

                state.shouldShowFullLoading() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionsList(
    transactions: List<Transaction>,
    viewModel: TransactionsViewModel
) {
    LazyColumn(modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = {
            items(transactions.size) {
                TransactionCard(
                    transaction = transactions[it],
                    viewModel = viewModel
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
            }
        })
}

@Composable
private fun NoTransactionsStub() {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No Transactions yet", style = MaterialTheme.typography.titleLarge)
    }
}