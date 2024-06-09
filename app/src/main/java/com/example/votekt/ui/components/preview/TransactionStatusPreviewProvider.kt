package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.votekt.domain.transactions.TransactionStatus

class TransactionStatusPreviewProvider : PreviewParameterProvider<TransactionStatus> {
    override val values: Sequence<TransactionStatus>
        get() = sequenceOf(
            TransactionStatus.MINED,
            TransactionStatus.PENDING,
            TransactionStatus.REVERTED,
        )
}
