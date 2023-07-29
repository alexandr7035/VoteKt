package com.example.votekt.ui.tx_history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.isActive
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
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

    fun loadTransactionList() {
        viewModelScope.launch {
            delay(500)

            val txs = transactionRepository.getTransactions()
            _state.value = _state.value.copy(
                isLoading = false,
                data = txs,
                error = null
            )
        }
    }

    fun observeTransactionStatus(hash: String): StateFlow<TxStatus> {
        Log.d("DEBUG_TAG", "subscribe for ${hash}")

        return flow {
            while (currentCoroutineContext().isActive) {
                val res = transactionRepository.refreshTxStatus(hash)

                when (res) {
                    is OperationResult.Success -> {
                        emit(res.data)
                    }

                    is OperationResult.Failure -> {
                        // TODO
                    }
                }
            }

        }.conflate().stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), TxStatus.PENDING)
    }
}