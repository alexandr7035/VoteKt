package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import kotlinx.coroutines.flow.Flow
import org.kethereum.model.Address

interface AccountRepository {
    suspend fun getSelfAddress(): Address
    suspend fun isAccountPresent(): Boolean
    suspend fun createAndSaveAccount(seedPhrase: List<MnemonicWord>)

    fun observeAccountBalance(): Flow<Wei>

    suspend fun refreshBalance()

    suspend fun clearAccount()
}
