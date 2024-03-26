package com.example.votekt.domain.transactions

import by.alexandr7035.ethereum.model.Wei
import org.kethereum.model.Address

sealed class ConfirmTransactionState {
    object Idle: ConfirmTransactionState()

    data class TxReview(
        val data: PrepareTransactionData,
        val transactionType: TransactionType,
        val to: Address,
        val value: Wei?,
        val input: String?,
        val totalEstimatedFee: Wei?,
        val minerTipFee: Wei?,
        val isSufficientBalance: Boolean?,
    ): ConfirmTransactionState()
}