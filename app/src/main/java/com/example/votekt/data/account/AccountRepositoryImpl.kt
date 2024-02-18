package com.example.votekt.data.account

import android.util.Log
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import cash.z.ecc.android.bip39.Mnemonics
import com.example.votekt.data.account.mnemonic.Word
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
    private val ethereumClient: EthereumClient,
    private val cryptoHelper: CryptoHelper
) : AccountRepository {
    override fun getAccountBalance(): Flow<Wei> = flow {
        while (coroutineContext.isActive) {
            emit(ethereumClient.getBalance(
                address = Address("0x3C44CdDdB6a900fa2b585dd299e03d12FA4293BC")
            ))
            delay(balancePollingDelay)
        }
    }.flowOn(dispatcher)

    override suspend fun createAndSaveAccount(seedPhrase: List<Word>) {
        val rawPhrase = seedPhrase.joinToString(" ") { it.value }
        val mnemonicCode = Mnemonics.MnemonicCode(rawPhrase)
        val credentials = cryptoHelper.generateCredentialsFromMnemonic(mnemonicCode)
        Log.d("WEB3_TAG", "address ${credentials.address} ${credentials.privateKey}")
    }
}