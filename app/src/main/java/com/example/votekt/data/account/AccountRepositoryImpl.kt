package com.example.votekt.data.account

import com.example.votekt.data.web3_core.utils.toEther
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

class AccountRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val balancePollingDelay: Duration,
    private val web3Client: Web3j
) : AccountRepository {
    override fun getAccountBalance(): Flow<AccountBalance> = flow {
        while (coroutineContext.isActive) {
            val balance = web3Client.ethGetBalance(
                "0x3C44CdDdB6a900fa2b585dd299e03d12FA4293BC",
                blockParam
            ).send()

            if (balance.hasError().not()) {
                emit(AccountBalance(
                    amount = balance.balance.toEther()
                ))
            }

            delay(balancePollingDelay)
        }
    }.flowOn(dispatcher)

    companion object {
        private val blockParam = DefaultBlockParameterName.LATEST
    }
}