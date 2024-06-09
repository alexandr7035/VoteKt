package com.example.votekt.ui.feature.transactions.review

import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.R
import com.example.votekt.domain.transactions.ReviewTransactionData
import com.example.votekt.domain.transactions.TransactionEstimationError
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature.applock.core.BiometricsPromptUi
import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.consumed
import org.kethereum.model.Address

data class ReviewTransactionDataUi(
    val transactionType: TransactionType,
    val value: Wei?,
    val recipient: Address,
    val totalEstimatedFee: Wei?,
    val minerTipFee: Wei?,
    val error: UiText?,
    val showBiometricConfirmationEvent: StateEvent = consumed,
    val biometricsPromptState: BiometricsPromptUi = BiometricsPromptUi(
        title = UiText.StringResource(R.string.unlock_app_biometrics),
        cancelBtnText = UiText.StringResource(R.string.cancel)
    ),
    val isConfirmBtnEnabled: Boolean,
)

fun ReviewTransactionData.mapToUi(): ReviewTransactionDataUi {
    return ReviewTransactionDataUi(
        recipient = this.to,
        value = this.value,
        minerTipFee = this.minerTipFee,
        totalEstimatedFee = this.totalEstimatedFee,
        transactionType = this.transactionType,
        error = this.estimationError?.mapToUi(),
        isConfirmBtnEnabled = this.estimationError == null && this.totalEstimatedFee != null
    )
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
