package by.alexandr7035.votekt.domain.usecase.account

import by.alexandr7035.votekt.domain.repository.AccountRepository
import by.alexandr7035.votekt.domain.repository.TransactionRepository
import by.alexandr7035.votekt.domain.repository.VotingContractRepository

class LogoutUseCase(
    private val accountRepository: AccountRepository,
    private val votingContractRepository: VotingContractRepository,
    private val transactionRepository: TransactionRepository,
) {
    suspend fun invoke() {
        votingContractRepository.clearContractData()
        transactionRepository.clearTransactions()

        // Must be last as clears preferences which may be used in observables
        accountRepository.clearAccount()
    }
}
