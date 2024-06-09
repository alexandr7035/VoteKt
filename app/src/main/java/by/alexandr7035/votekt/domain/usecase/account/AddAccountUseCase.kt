package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.account.AccountRepository
import by.alexandr7035.votekt.domain.account.MnemonicWord

class AddAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(seed: List<MnemonicWord>) {
        accountRepository.createAndSaveAccount(seed)
    }
}
