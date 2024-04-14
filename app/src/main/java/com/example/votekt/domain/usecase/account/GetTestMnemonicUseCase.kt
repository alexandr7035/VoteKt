package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.MnemonicRepository
import com.example.votekt.domain.account.MnemonicWord

class GetTestMnemonicUseCase(
    private val mnemonicRepository: MnemonicRepository
) {
    fun invoke(): List<MnemonicWord> {
        return mnemonicRepository.getTestMnemonic()
    }
}