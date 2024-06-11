package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository
import by.alexandr7035.votekt.domain.repository.AppLockRepository
import by.alexandr7035.votekt.domain.repository.TransactionRepository
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

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
