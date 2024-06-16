package by.alexandr7035.votekt.domain.usecase.transactions

import by.alexandr7035.votekt.domain.repository.TransactionRepository

class ClearTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    suspend fun invoke() {
        return transactionRepository.clearTransactions()
    }
}
