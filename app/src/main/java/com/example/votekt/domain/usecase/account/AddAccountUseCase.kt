package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.account.MnemonicWord

class AddAccountUseCase(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(seed: List<MnemonicWord>) {
        accountRepository.createAndSaveAccount(seed)
    }
}
