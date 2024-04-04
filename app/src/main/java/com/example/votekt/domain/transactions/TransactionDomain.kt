package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.Wei

data class TransactionDomain(
    val type: TransactionType,
    val hash: String,
    val status: TransactionStatus,
    val dateSent: Long,
    val gasFee: Wei?,
) {
    companion object
}

fun TransactionDomain.isNotPendingOrCompleted(): Boolean {
    return this.status != TransactionStatus.MINED && this.status != TransactionStatus.PENDING
}