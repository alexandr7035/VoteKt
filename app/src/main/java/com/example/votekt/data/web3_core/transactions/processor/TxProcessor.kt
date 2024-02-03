package com.example.votekt.data.web3_core.transactions.processor

import com.example.votekt.data.web3_core.transactions.TxResult
import kotlinx.coroutines.flow.Flow

interface TxProcessor {
    fun observeTransactionResult(): Flow<TxResult>
}