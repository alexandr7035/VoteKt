package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.votekt.ui.voting_details.TransactionStatusUi

class TransactionStatusPreviewProvider: PreviewParameterProvider<TransactionStatusUi> {
    override val values: Sequence<TransactionStatusUi>
        get() = sequenceOf(
            TransactionStatusUi.CURRENTLY_AWAITED,
            TransactionStatusUi.PENDING,
            TransactionStatusUi.FAILED,
            TransactionStatusUi.CONFIRMED,
        )
}