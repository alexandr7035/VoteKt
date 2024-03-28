package com.example.votekt.domain.transactions

import kotlinx.coroutines.flow.StateFlow

interface SendTransactionRepository {
    val state: StateFlow<ConfirmTransactionState>

    suspend fun requirePrepareTransaction(data: PrepareTransactionData)

    suspend fun confirmTransaction()

    suspend fun cancelTransaction()
}