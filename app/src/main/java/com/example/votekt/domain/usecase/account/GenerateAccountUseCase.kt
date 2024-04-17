package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.MnemonicRepository
import com.example.votekt.domain.account.MnemonicWord

class GenerateAccountUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(wordCount: Int = 12): List<MnemonicWord> {
        return mnemonicRepository.generateMnemonic(wordCount)
    }
}