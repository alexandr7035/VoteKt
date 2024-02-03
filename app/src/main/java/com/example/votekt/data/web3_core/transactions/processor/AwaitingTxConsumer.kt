package com.example.votekt.data.web3_core.transactions.processor

import com.example.votekt.data.web3_core.transactions.TxResult

interface AwaitingTxConsumer {
    fun checkForTransaction(): TxResult
}