package com.example.votekt.ui.tx_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.model.Transaction
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        ScreenState<List<Transaction>>(
            data = null,
            error = null,
            isLoading = true
        )
    )

    val state = _state.asStateFlow()

    fun load() {
        viewModelScope.launch {
            // FIXME
            delay(1000)

            val txs = transactionRepository.getTransactions()
            _state.value = _state.value.copy(
                isLoading = false,
                data = txs,
                error = null
            )
        }
    }
}