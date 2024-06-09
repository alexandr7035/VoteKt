package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.account.MnemonicRepository
import by.alexandr7035.votekt.domain.account.MnemonicWord

class GenerateAccountUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(wordCount: Int = 12): List<MnemonicWord> {
        return mnemonicRepository.generateMnemonic(wordCount)
    }
}