package com.example.votekt.data.account

import by.alexandr7035.ethereum.core.EthereumRepository
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

class AccountRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val balancePollingDelay: Duration,
    private val ethereumRepository: EthereumRepository,
) : AccountRepository {
    override fun getAccountBalance(): Flow<Wei> = flow {
        while (coroutineContext.isActive) {
            emit(ethereumRepository.getBalance(
                address = Address("0x3C44CdDdB6a900fa2b585dd299e03d12FA4293BC")
            ))
            delay(balancePollingDelay)
        }
    }.flowOn(dispatcher)
}