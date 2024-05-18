package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.ETHER
import by.alexandr7035.ethereum.model.GWEI
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionDataUi
import org.kethereum.model.Address

class TransactionReviewPreviewProvider : PreviewParameterProvider<ReviewTransactionDataUi> {
    override val values: Sequence<ReviewTransactionDataUi>
        get() = sequenceOf(
            ReviewTransactionDataUi(
                transactionType = TransactionType.PAYMENT,
                recipient = Address(SAMPLE_ADDRESS),
                value = 0.24.ETHER,
                minerTipFee = 1.GWEI,
                totalEstimatedFee = 30.GWEI,
                error = UiText.DynamicString("Insufficient balance"),
                isConfirmBtnEnabled = false,
            ),
            ReviewTransactionDataUi(
                transactionType = TransactionType.VOTE,
                recipient = Address(SAMPLE_ADDRESS),
                minerTipFee = 1.GWEI,
                totalEstimatedFee = 30.GWEI,
                error = null,
                value = 0.1.ETHER,
                isConfirmBtnEnabled = true,
            )
        )

    private companion object {
        const val SAMPLE_ADDRESS = "0xb794f5ea0ba39494ce839613fffba74279579268"
    }
}
