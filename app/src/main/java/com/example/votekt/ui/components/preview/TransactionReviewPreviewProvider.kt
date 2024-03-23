package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.ETHER
import by.alexandr7035.ethereum.model.GWEI
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionData
import org.kethereum.model.Address

class TransactionReviewPreviewProvider : PreviewParameterProvider<ReviewTransactionData> {
    override val values: Sequence<ReviewTransactionData>
        get() = sequenceOf(
            ReviewTransactionData.SendAmount(
                transactionType = TransactionType.PAYMENT,
                recipient = Address(SAMPLE_ADDRESS),
                value = 0.24.ETHER,
                minerTipFee = 1.GWEI,
                totalEstimatedFee = 30.GWEI,
                isBalanceSufficient = true,
            ),
            ReviewTransactionData.ContractInteraction(
                transactionType = TransactionType.VOTE,
                recipient = Address(SAMPLE_ADDRESS),
                contractInput = SAMPLE_CONTRACT_INPUT,
                minerTipFee = 1.GWEI,
                totalEstimatedFee = 30.GWEI,
                isBalanceSufficient = false
            )
        )

    private companion object {
        const val SAMPLE_CONTRACT_INPUT = "0x67043cae0000000000000000000000005a9dac9315fdd1c3d13ef8af7fdfeb522db08f020000000000000000000000000000000000000000000000000000000058a2023000000000000000000000000"
        const val SAMPLE_ADDRESS = "0xb794f5ea0ba39494ce839613fffba74279579268"
    }
}
