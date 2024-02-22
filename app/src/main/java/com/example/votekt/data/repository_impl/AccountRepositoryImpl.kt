package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum.model.Wei
import cash.z.ecc.android.bip39.Mnemonics
import com.cioccarellia.ksprefs.KsPrefs
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.data.cache.PrefKeys
import com.example.votekt.domain.account.AccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.math.BigInteger
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
        val recentBalance = ksPrefs.pull(PrefKeys.RECENT_BALANCE, "")
        if (recentBalance.isNotBlank()) {
            // TODO extensions
            emit(Wei(BigInteger(recentBalance)))
        }

        while (coroutineContext.isActive) {
            val updatedBalance = ethereumClient.getBalance(
                address = Address(ksPrefs.pull(PrefKeys.ACCOUNT_ADDRESS_KEY))
            )
            ksPrefs.push(PrefKeys.RECENT_BALANCE, updatedBalance.value.toString())
            emit(updatedBalance)
            delay(balancePollingDelay)
        }
    }.flowOn(dispatcher)

    override suspend fun getSelfAddress(): Address {
        return Address(ksPrefs.pull(PrefKeys.ACCOUNT_ADDRESS_KEY, ""))
    }

    override suspend fun createAndSaveAccount(seedPhrase: List<MnemonicWord>) {
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