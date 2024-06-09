package by.alexandr7035.votekt.domain.usecase.debug

import by.alexandr7035.votekt.domain.account.MnemonicRepository
import by.alexandr7035.votekt.domain.account.MnemonicWord

class GetTestMnemonicUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(): List<MnemonicWord> {
        return mnemonicRepository.getTestMnemonic()
    }
}
