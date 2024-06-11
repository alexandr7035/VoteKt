package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository
import org.kethereum.model.Address

class GetSelfAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(): Address {
        return accountRepository.getSelfAddress()
    }
}