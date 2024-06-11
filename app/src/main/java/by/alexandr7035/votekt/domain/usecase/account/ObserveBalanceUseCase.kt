package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow

class ObserveBalanceUseCase(
    private val accountRepository: AccountRepository,
) {
    fun invoke(): Flow<Wei> {
        return accountRepository.observeAccountBalance()
    }
}