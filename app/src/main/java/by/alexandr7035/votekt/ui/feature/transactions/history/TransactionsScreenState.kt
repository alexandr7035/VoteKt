package by.alexandr7035.votekt.ui.feature.transactions.history

import by.alexandr7035.votekt.domain.model.transactions.TransactionDomain
import by.alexandr7035.votekt.ui.UiErrorMessage
import by.alexandr7035.votekt.ui.feature.transactions.history.model.TransactionsScreenNavigationEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class TransactionsScreenState(
    val transactions: List<TransactionDomain> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiErrorMessage? = null,
    val navigationEvent: StateEventWithContent<TransactionsScreenNavigationEvent> = consumed()
)
