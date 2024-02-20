package com.example.votekt.data.model

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.data.web3_core.transactions.TxStatus

// TODO refactoring: get rid of extra tx models
data class Transaction(
    val type: TransactionType,
    val hash: String,
    val status: TxStatus,
    val dateSent: Long,
    val gasFee: Wei?,
) {
    companion object
}