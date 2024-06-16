package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.model.account.MnemonicWordConfirm
import by.alexandr7035.votekt.domain.repository.MnemonicRepository

class GetAccountConfirmationData(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(mnemonic: List<MnemonicWord>): List<MnemonicWordConfirm> {
        return mnemonicRepository.getRandomMnemonicWords(mnemonic)
    }
}
