package com.example.votekt.ui.tx_history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.OperationResult
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.web3_core.transactions.TxStatus
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val CHECK_FOR_TX_DELAY = 10_000L

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
            val txs = transactionRepository.getTransactions()
            _state.value = _state.value.copy(
                isLoading = false,
                data = txs,
                error = null
            )
        }
    }

    fun clearTransactions() {
        viewModelScope.launch {
            transactionRepository.clearTransactions()
            loadTransactionList()
        }
    }

    fun observeTransactionStatus(hash: String): StateFlow<TxStatus> {
        Log.d("DEBUG_TAG", "subscribe for tx receipt ${hash}")

        return flow {
            while (currentCoroutineContext().isActive) {
                val res = transactionRepository.refreshTxStatus(hash)

                when (res) {
                    is OperationResult.Success -> {
                        emit(res.data)

                        if (res.data != TxStatus.PENDING) {
                            break
                        }
                    }

                    is OperationResult.Failure -> {
                        // TODO
                    }
                }

                delay(CHECK_FOR_TX_DELAY)
            }

        }.conflate().stateIn(viewModelScope, SharingStarted.WhileSubscribed(0), TxStatus.PENDING)
    }
}