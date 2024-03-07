package com.example.votekt.ui.utils

import androidx.annotation.DrawableRes
import com.example.votekt.R
import com.example.votekt.domain.transactions.TransactionStatus

data class TransactionStatusUi(
    val status: String,
    val statusExplained: String,
    @DrawableRes val iconRes: Int
)

fun TransactionStatus.getTransactionStatusUi(): TransactionStatusUi {
    return when (this) {
        TransactionStatus.PENDING -> TransactionStatusUi(
            status  = "Pending",
            statusExplained = "Transaction is pending on blockchain",
            iconRes = R.drawable.ic_status_pending
        )
        TransactionStatus.MINED -> TransactionStatusUi(
            status  = "Mined",
            statusExplained = "Transaction is confirmed. You can view it in explorer",
            iconRes = R.drawable.ic_status_accepted
        )
        TransactionStatus.REVERTED -> TransactionStatusUi(
            status  = "Failed",
            statusExplained = "Transaction is failed. Check balance and try again",
            iconRes = R.drawable.ic_status_rejected
        )
    }
}
