package by.alexandr7035.votekt.domain.usecase.debug

import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.repository.MnemonicRepository

class GetTestMnemonicUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(): List<MnemonicWord> {
        return mnemonicRepository.getTestMnemonic()
    }
}
