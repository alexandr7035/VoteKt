package com.example.votekt.data.account

import android.util.Log
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import cash.z.ecc.android.bip39.Mnemonics
import com.cioccarellia.ksprefs.KsPrefs
import com.example.votekt.data.account.mnemonic.Word
import com.example.votekt.data.model.PrefKeys
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
    private val cryptoHelper: CryptoHelper,
    private val ksPrefs: KsPrefs,
) : AccountRepository {

    override suspend fun isAccountPresent(): Boolean {
        return ksPrefs.pull(PrefKeys.ACCOUNT_ADDRESS_KEY, "").isNotBlank()
    }

    override fun getAccountBalance(): Flow<Wei> = flow {
        while (coroutineContext.isActive) {
            emit(ethereumClient.getBalance(
                address = Address(ksPrefs.pull(PrefKeys.ACCOUNT_ADDRESS_KEY))
            ))
            delay(balancePollingDelay)
        }
    }.flowOn(dispatcher)

    override suspend fun getSelfAddress(): Address {
        return Address(ksPrefs.pull(PrefKeys.ACCOUNT_ADDRESS_KEY))
    }

    override suspend fun createAndSaveAccount(seedPhrase: List<Word>) {
        val rawPhrase = seedPhrase.joinToString(" ") { it.value }
        val mnemonicCode = Mnemonics.MnemonicCode(rawPhrase)
        val credentials = cryptoHelper.generateCredentialsFromMnemonic(mnemonicCode)

        with(ksPrefs) {
            push(PrefKeys.ACCOUNT_ADDRESS_KEY, credentials.address)
            push(PrefKeys.PRIVATE_KEY, credentials.privateKey)
        }

        Log.d("WEB3_TAG", "created account ${credentials.address} ${credentials.privateKey}")
    }
}