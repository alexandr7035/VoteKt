package by.alexandr7035.votekt.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.model.transactions.TransactionDomain
import by.alexandr7035.votekt.domain.model.transactions.TransactionStatus
import by.alexandr7035.votekt.domain.model.transactions.TransactionType
import java.math.BigInteger

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val hash: String,
    val type: TransactionType,
    val status: TransactionStatus,
    val dateSent: Long,
    val value: Wei?,
    val gasUsed: BigInteger?,
    val gasFee: Wei?,
) {
    fun mapToData() = TransactionDomain(
        type = type,
        hash = hash,
        status = status,
        dateSent = dateSent,
        gasFee = gasFee,
        value = value,
    )
}
