package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository

class RefreshBalanceUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke() {
        accountRepository.refreshBalance()
    }
}
