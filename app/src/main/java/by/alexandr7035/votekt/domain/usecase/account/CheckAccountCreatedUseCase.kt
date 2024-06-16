package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository

class CheckAccountCreatedUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(): Boolean {
        return accountRepository.isAccountPresent()
    }
}
