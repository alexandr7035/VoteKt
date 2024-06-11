package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository
import by.alexandr7035.votekt.domain.model.account.MnemonicWord

class AddAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(seed: List<MnemonicWord>) {
        accountRepository.createAndSaveAccount(seed)
    }
}
