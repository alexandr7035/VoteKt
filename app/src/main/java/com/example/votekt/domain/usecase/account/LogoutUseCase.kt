package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.votings.VotingContractRepository

class LogoutUseCase(
    private val accountRepository: AccountRepository,
    private val votingContractRepository: VotingContractRepository,
) {
    suspend fun invoke() {
        accountRepository.clearAccount()
        votingContractRepository.clearContractData()
    }
}