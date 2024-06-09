package by.alexandr7035.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.votekt.domain.transactions.TransactionStatus

class TransactionStatusPreviewProvider : PreviewParameterProvider<TransactionStatus> {
    override val values: Sequence<TransactionStatus>
        get() = sequenceOf(
            TransactionStatus.MINED,
            TransactionStatus.PENDING,
            TransactionStatus.REVERTED,
        )
}
