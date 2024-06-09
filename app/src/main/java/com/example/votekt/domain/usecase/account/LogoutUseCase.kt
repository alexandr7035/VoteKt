package com.example.votekt.domain.usecase.account

import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.security.AppLockRepository
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.votings.VotingContractRepository

class LogoutUseCase(
    private val accountRepository: AccountRepository,
    private val votingContractRepository: VotingContractRepository,
    private val transactionRepository: TransactionRepository,
    private val appLockRepository: AppLockRepository,
) {
    suspend fun invoke() {
        accountRepository.clearAccount()
        votingContractRepository.clearContractData()
        transactionRepository.clearTransactions()
        appLockRepository.removeAppLock()
    }
}
