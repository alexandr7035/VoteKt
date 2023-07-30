package com.example.votekt.data.model

data class Transaction(
    val type: TransactionType,
    val hash: String,
    val status: TxStatus,
    val dateSent: Long
) {
    companion object {
        fun mock() = Transaction(
            type = TransactionType.CREATE_PROPOSAL,
            hash = "abcdef1234",
            status = TxStatus.MINED,
            dateSent = 0
        )
    }
}