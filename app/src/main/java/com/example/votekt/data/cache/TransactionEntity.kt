package com.example.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.TransactionStatus
import java.math.BigInteger

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val hash: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val dateSent: Long,
    val gasUsed: BigInteger?,
    val gasFee: Wei?,
) {
    fun mapToData() = TransactionDomain(
        type = type,
        hash = hash,
        status = status,
        dateSent = dateSent,
        gasFee = gasFee
    )
}
