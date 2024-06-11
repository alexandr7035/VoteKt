package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.votekt.domain.model.transactions.PrepareTransactionData
import by.alexandr7035.votekt.domain.model.transactions.ReviewTransactionData
import kotlinx.coroutines.flow.StateFlow

interface SendTransactionRepository {
    val state: StateFlow<ReviewTransactionData?>

    suspend fun requirePrepareTransaction(data: PrepareTransactionData)

    suspend fun confirmCurrentTransaction()

    suspend fun cancelCurrentTransaction()
}
