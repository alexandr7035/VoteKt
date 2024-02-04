package com.example.votekt.data.web3_core.transactions.processor

import com.example.votekt.data.web3_core.transactions.TxHash
import com.example.votekt.data.web3_core.transactions.TxResult
import kotlinx.coroutines.flow.Flow

interface TxProcessor {
    fun observeTransactionResult(hash: TxHash): Flow<TxResult>
}