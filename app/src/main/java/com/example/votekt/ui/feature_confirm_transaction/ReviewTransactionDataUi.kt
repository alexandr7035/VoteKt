package com.example.votekt.ui.feature_confirm_transaction

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.R
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.transactions.TransactionEstimationError
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.isContractInteraction
import com.example.votekt.ui.core.resources.UiText
import org.kethereum.model.Address

sealed class ReviewTransactionDataUi(
    open val transactionType: TransactionType,
    open val recipient: Address,
    open val totalEstimatedFee: Wei?,
    open val minerTipFee: Wei?,
    open val estimationError: UiText?,
) {
    data class SendAmount(
        val value: Wei,
        override val transactionType: TransactionType,
        override val recipient: Address,
        override val totalEstimatedFee: Wei?,
        override val minerTipFee: Wei?,
        override val estimationError: UiText?,
    ): ReviewTransactionDataUi(
        recipient = recipient,
        totalEstimatedFee = totalEstimatedFee,
        minerTipFee = minerTipFee,
        transactionType = transactionType,
        estimationError = estimationError,
    )

    data class ContractInteraction(
        val contractInput: String,
        override val transactionType: TransactionType,
        override val recipient: Address,
        override val totalEstimatedFee: Wei?,
        override val minerTipFee: Wei?,
        override val estimationError: UiText?,
    ): ReviewTransactionDataUi(
        recipient = recipient,
        totalEstimatedFee = totalEstimatedFee,
        minerTipFee = minerTipFee,
        transactionType = transactionType,
        estimationError = estimationError,
    )
}

fun ReviewTransactionDataUi.canSendTransaction() = this.totalEstimatedFee != null && this.estimationError == null

fun ReviewTransactionData.mapToUi(): ReviewTransactionDataUi {
    return when {
        this.transactionType.isContractInteraction() -> {
            ReviewTransactionDataUi.ContractInteraction(
                recipient = this.to,
                contractInput = this.input!!,
                minerTipFee = this.minerTipFee,
                totalEstimatedFee = this.totalEstimatedFee,
                transactionType = this.transactionType,
                estimationError = this.estimationError?.mapToUi(),
            )
        }

        else -> {
            ReviewTransactionDataUi.SendAmount(
                recipient = this.to,
                value = this.value!!,
                minerTipFee = this.minerTipFee,
                totalEstimatedFee = this.totalEstimatedFee,
                transactionType = this.transactionType,
                estimationError = this.estimationError?.mapToUi(),
            )
        }
    }
}

private fun TransactionEstimationError.mapToUi(): UiText {
    return when (this) {
        is TransactionEstimationError.InsufficientBalance -> { 
            UiText.StringResource(R.string.insufficient_balance)
        }
        is TransactionEstimationError.ExecutionError -> {
            this.message?.let {
                UiText.DynamicString(it)
            } ?: run {
                UiText.StringResource(R.string.unknown_transaction_execution_error)
            }
        }
    }
}