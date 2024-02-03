package com.example.votekt.data.web3_core.transactions

import org.web3j.protocol.core.methods.response.TransactionReceipt


sealed class TxResult {
    object AwaitingResult: TxResult()
    data class Success(val receipt: TransactionReceipt): TxResult()
    object TimeExceeded: TxResult()
    // TODO error type
    data class Error(val errorType: Exception): TxResult()
}
