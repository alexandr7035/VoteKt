package by.alexandr7035.votekt.domain.usecase.transactions

import by.alexandr7035.votekt.domain.repository.SendTransactionRepository

class ConfirmOutgoingTransactionUseCase(
    private val sendTransactionRepository: SendTransactionRepository,
) {
    suspend fun invoke() {
        return sendTransactionRepository.confirmCurrentTransaction()
    }
}