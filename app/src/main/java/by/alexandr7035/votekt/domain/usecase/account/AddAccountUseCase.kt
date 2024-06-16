package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.repository.AccountRepository

class AddAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(seed: List<MnemonicWord>) {
        accountRepository.createAndSaveAccount(seed)
    }
}
