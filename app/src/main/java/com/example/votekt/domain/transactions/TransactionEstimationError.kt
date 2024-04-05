package com.example.votekt.domain.transactions

sealed class TransactionEstimationError(open val message: String?) {
    object InsufficientBalance: TransactionEstimationError(null)
    data class ExecutionError(override val message: String?): TransactionEstimationError(message)
}