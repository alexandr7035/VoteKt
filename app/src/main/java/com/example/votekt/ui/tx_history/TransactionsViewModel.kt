package com.example.votekt.ui.tx_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.core.AppError
import com.example.votekt.ui.core.ScreenState
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
    private val _state = MutableStateFlow(
        ScreenState<List<TransactionDomain>>(
            data = null,
            error = null,
            isLoading = true
        )
    )

    val state = _state.asStateFlow()

    init {
        transactionRepository
            .getTransactions()
            .onEach { transactions ->
                _state.update {
                    it.copy(
                        data = transactions,
                        error = null,
                        isLoading = false
                    )
                }
            }
            .catch {error ->
                _state.update {
                    it.copy(
                        error = AppError.fromThrowable(error),
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun clearTransactions() {
        viewModelScope.launch {
            transactionRepository.clearTransactions()
        }
    }
}