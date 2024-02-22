package com.example.votekt.domain.account

import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun getSelfAddress(): Address
    suspend fun isAccountPresent(): Boolean
    suspend fun createAndSaveAccount(seedPhrase: List<MnemonicWord>)

    fun getAccountBalance(): Flow<Wei>
}