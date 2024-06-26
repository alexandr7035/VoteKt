package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.MnemonicRepository

class VerifyMnemonicPhraseUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(words: List<String>) {
        return mnemonicRepository.verifyMnemonic(words)
    }
}
