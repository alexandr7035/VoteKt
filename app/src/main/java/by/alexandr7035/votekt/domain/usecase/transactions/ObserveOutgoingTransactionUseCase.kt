package by.alexandr7035.votekt.domain.usecase.transactions

import by.alexandr7035.votekt.domain.model.transactions.ReviewTransactionData
import by.alexandr7035.votekt.domain.repository.SendTransactionRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveOutgoingTransactionUseCase(
    private val sendTransactionRepository: SendTransactionRepository
) {
    fun invoke(): StateFlow<ReviewTransactionData?> {
        return sendTransactionRepository.state
    }
}
