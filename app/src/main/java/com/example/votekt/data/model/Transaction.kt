package com.example.votekt.data.model

data class Transaction(
    val hash: String,
    val status: TxStatus,
    val dateSent: Long
) {
    companion object {
        fun mock() = Transaction(hash = "abcdef1234", status = TxStatus.CONFIRMED, dateSent = 0)
    }
}