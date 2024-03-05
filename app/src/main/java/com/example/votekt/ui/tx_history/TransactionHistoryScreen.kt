package com.example.votekt.ui.tx_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.TransactionCard
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.uiError
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionHistoryScreen(viewModel: TransactionsViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.transactions),
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
                    // FIXME ui state
                    ErrorFullScreen(error = state.error?.errorType?.uiError!!, onRetry = {
                        // TODO
//                        viewModel.loadTransactionList()
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
    transactions: List<TransactionDomain>,
    viewModel: TransactionsViewModel
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp))
    {
        items(transactions.size) {
            TransactionCard(
                transaction = transactions[it]
            )
        }
    }
}

@Composable
private fun NoTransactionsStub() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_transactions_yet),
            style = MaterialTheme.typography.titleLarge
        )
    }
}