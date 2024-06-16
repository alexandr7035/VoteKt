package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.model.account.MnemonicWordConfirm
import by.alexandr7035.votekt.domain.repository.MnemonicRepository

class ConfirmNewAccountUseCase(
    private val mnemonicRepository: MnemonicRepository,
) {
    fun invoke(
        mnemonic: List<MnemonicWord>,
        proposedWordsToConfirm: List<MnemonicWordConfirm>,
        confirmationData: Map<Int, MnemonicWord>
    ) {
        return mnemonicRepository.confirmPhrase(mnemonic, proposedWordsToConfirm, confirmationData)
    }
}
