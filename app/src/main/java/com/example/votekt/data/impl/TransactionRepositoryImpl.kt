package com.example.votekt.data.impl

import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.cache.TransactionDao
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TxStatus

class TransactionRepositoryImpl(private val txDao: TransactionDao) : TransactionRepository {
    override fun getTransactions(): List<Transaction> {
        return List(5) {
            Transaction(hash = "abcde1345", dateSent = 0, status = TxStatus.CONFIRMED)
        }
    }
}