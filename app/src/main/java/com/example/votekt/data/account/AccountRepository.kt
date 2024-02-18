package com.example.votekt.data.account

import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.data.account.mnemonic.Word
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun getSelfAddress(): Address
    suspend fun isAccountPresent(): Boolean
    suspend fun createAndSaveAccount(seedPhrase: List<Word>)

    fun getAccountBalance(): Flow<Wei>
}