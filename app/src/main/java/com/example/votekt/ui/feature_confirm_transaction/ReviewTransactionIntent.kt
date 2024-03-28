package com.example.votekt.ui.feature_confirm_transaction

sealed class ReviewTransactionIntent {
    object LoadTransactionData: ReviewTransactionIntent()
    object SubmitTransaction: ReviewTransactionIntent()
    object DismissDialog: ReviewTransactionIntent()
}
