package com.example.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TransactionType
import com.example.votekt.data.web3_core.transactions.TxStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val hash: String,
    val type: TransactionType,
    val status: TxStatus,
    val dateSent: Long
) {
    fun mapToData() = Transaction(
        type = type,
        hash = hash,
        status = status,
        dateSent = dateSent,
    )
}
