package by.alexandr7035.votekt.ui.feature.transactions.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.alexandr7035.votekt.domain.core.ErrorType
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.domain.usecase.transactions.ClearTransactionsUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ObserveTransactionsUseCase
import by.alexandr7035.votekt.ui.feature.transactions.history.model.TransactionsScreenIntent
import by.alexandr7035.votekt.ui.feature.transactions.history.model.TransactionsScreenNavigationEvent
import by.alexandr7035.votekt.ui.uiError
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val observeTransactionsUseCase: ObserveTransactionsUseCase,
    private val clearTransactionsUseCase: ClearTransactionsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsScreenState())

    val state = _state.asStateFlow()

    init {
        subscribeToTransactions()
    }

    private fun subscribeToTransactions() {
        observeTransactionsUseCase.invoke()
            .onEach { transactions ->
                _state.update {
                    it.copy(
                        transactions = transactions,
                        error = null,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
                _state.update {
                    it.copy(
                        error = ErrorType.fromThrowable(error).uiError,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: TransactionsScreenIntent) {
        when (intent) {
            is TransactionsScreenIntent.ExplorerUrlClick -> _state.update {
                it.copy(
                    navigationEvent = triggered(
                        TransactionsScreenNavigationEvent.ToExplorer(intent.payload, intent.exploreType)
                    )
                )
            }

            is TransactionsScreenIntent.ClearClick -> onClearTransactions()
            is TransactionsScreenIntent.RetryErrorClick -> subscribeToTransactions()
        }
    }

    private fun onClearTransactions() {
        viewModelScope.launch {
            OperationResult.runWrapped {
                clearTransactionsUseCase.invoke()
            }
        }
    }

    fun consumeNavigationEvent() {
        _state.update { it.copy(navigationEvent = consumed()) }
    }
}
