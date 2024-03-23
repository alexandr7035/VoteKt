package com.example.votekt.ui.feature_confirm_transaction

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.transactions.TransactionType
import org.kethereum.model.Address

data class ReviewTransactionState(
    val data: ReviewTransactionData? = null
)

sealed class ReviewTransactionData(
    open val transactionType: TransactionType,
    open val recipient: Address,
    open val totalEstimatedFee: Wei?,
    open val minerTipFee: Wei?,
    open val isBalanceSufficient: Boolean?,
) {
    data class SendAmount(
        val value: Wei,
        override val transactionType: TransactionType,
        override val recipient: Address,
        override val totalEstimatedFee: Wei?,
        override val minerTipFee: Wei?,
        override val isBalanceSufficient: Boolean?,
    ): ReviewTransactionData(
        recipient = recipient,
        totalEstimatedFee = totalEstimatedFee,
        minerTipFee = minerTipFee,
        transactionType = transactionType,
        isBalanceSufficient = isBalanceSufficient,
    )

    data class ContractInteraction(
        val contractInput: String,
        override val transactionType: TransactionType,
        override val recipient: Address,
        override val totalEstimatedFee: Wei?,
        override val minerTipFee: Wei?,
        override val isBalanceSufficient: Boolean?,
    ): ReviewTransactionData(
        recipient = recipient,
        totalEstimatedFee = totalEstimatedFee,
        minerTipFee = minerTipFee,
        transactionType = transactionType,
        isBalanceSufficient = isBalanceSufficient
    )
}

fun ReviewTransactionData.canSendTransaction() = this.totalEstimatedFee != null && this.isBalanceSufficient == true

