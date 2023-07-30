package com.example.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.votekt.data.model.TransactionType
import com.example.votekt.data.model.TxStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val type: TransactionType,
    val hash: String,
    val status: TxStatus,
    val dateSent: Long
)
