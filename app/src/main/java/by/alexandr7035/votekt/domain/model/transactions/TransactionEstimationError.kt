package by.alexandr7035.votekt.domain.model.transactions

sealed class TransactionEstimationError(open val message: String?) {
    object InsufficientBalance : TransactionEstimationError(null)
    data class ExecutionError(override val message: String?) : TransactionEstimationError(message)
}
