package com.example.votekt.ui.feature_transaction_history

import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.ui.UiErrorMessage
import com.example.votekt.ui.feature_transaction_history.model.TransactionsScreenNavigationEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class TransactionsScreenState(
    val transactions: List<TransactionDomain> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null,
    val navigationEvent: StateEventWithContent<TransactionsScreenNavigationEvent> = consumed()
)
