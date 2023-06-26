package com.example.votekt.ui.tx_history

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.model.Transaction

class TransactionsViewModel(
    private val transactionRepository: TransactionRepository
): ViewModel() {
    fun load(): List<Transaction> {
        Log.d("TEST", "${transactionRepository.getTransactions()}")
        return transactionRepository.getTransactions()
    }
}