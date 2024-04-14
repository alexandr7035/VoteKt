package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.MnemonicRepository

class VerifyMnemonicPhraseUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(words: List<String>) {
        return mnemonicRepository.verifyMnemonic(words)
    }
}
