package com.example.votekt.data.account

import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccountBalance(): Flow<AccountBalance>
}