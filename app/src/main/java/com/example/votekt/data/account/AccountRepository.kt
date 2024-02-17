package com.example.votekt.data.account

import by.alexandr7035.ethereum.model.Wei
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAccountBalance(): Flow<Wei>
}