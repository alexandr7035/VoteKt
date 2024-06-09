package com.example.votekt.ui.feature.transactions.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.ui.feature.transactions.history.model.TransactionsScreenIntent
import com.example.votekt.ui.feature.transactions.history.model.TransactionsScreenNavigationEvent
import com.example.votekt.ui.uiError
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
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsScreenState())

    val state = _state.asStateFlow()

    init {
        subscribeToTransactions()
    }

    private fun subscribeToTransactions() {
        transactionRepository
            .getTransactions()
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
            transactionRepository.clearTransactions()
        }
    }

    fun consumeNavigationEvent() {
        _state.update { it.copy(navigationEvent = consumed()) }
    }
}
