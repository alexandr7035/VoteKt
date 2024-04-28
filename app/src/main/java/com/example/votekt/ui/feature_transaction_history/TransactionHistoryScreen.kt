package com.example.votekt.ui.feature_transaction_history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.TransactionCard
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.feature_transaction_history.model.TransactionsScreenIntent
import com.example.votekt.ui.feature_transaction_history.model.TransactionsScreenNavigationEvent
import com.example.votekt.ui.theme.Dimensions
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionHistoryScreen(
    viewModel: TransactionsViewModel = koinViewModel(),
    onNavigationEvent: (TransactionsScreenNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Scaffold(topBar = {
        AppBar(
            title = stringResource(R.string.transactions),
            actions = {
                IconButton(
                    onClick = {
                        viewModel.onIntent(TransactionsScreenIntent.ClearClick)
                    }) {
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
                state.isLoading -> FullscreenProgressBar()
                state.error != null -> ErrorFullScreen(
                    error = state.error,
                    onRetry = {
                        // TODO
                    }
                )

                else -> {
                    if (state.transactions.isNotEmpty()) {
                        TransactionsList(
                            transactions = state.transactions,
                            onExplorerClick = { payload, exploreType ->
                                viewModel.onIntent(
                                    TransactionsScreenIntent.ExplorerUrlClick(payload, exploreType)
                                )
                            }
                        )
                    } else {
                        NoTransactionsStub()
                    }
                }
            }
        }
    }

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent,
    ) {
        onNavigationEvent(it)
    }
}

@Composable
private fun TransactionsList(
    transactions: List<TransactionDomain>,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(Dimensions.cardListSpacing),
        contentPadding = PaddingValues(
            horizontal = Dimensions.screenPaddingHorizontal,
            vertical = Dimensions.screenPaddingVertical,
        )
    )
    {
        items(transactions.size) {
            TransactionCard(
                transaction = transactions[it],
                onExplorerClick = onExplorerClick,
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