package com.example.votekt.data

import com.example.votekt.data.model.Transaction

interface TransactionRepository {
    fun getTransactions(): List<Transaction>
}