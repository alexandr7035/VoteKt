package by.alexandr7035.votekt.domain.usecase.transactions

import by.alexandr7035.votekt.domain.repository.TransactionRepository

class ObserveTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) {
    fun invoke() = transactionRepository.observeTransactions()
}