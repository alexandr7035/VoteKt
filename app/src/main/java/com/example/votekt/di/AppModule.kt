package com.example.votekt.di

import androidx.room.Room
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.crypto.CryptoHelperImpl
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.example.votekt.BuildConfig
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.VotingRepository
import com.example.votekt.data.account.AccountRepository
import com.example.votekt.data.account.AccountRepositoryImpl
import com.example.votekt.data.account.mnemonic.MnemonicHelperDebugImpl
import com.example.votekt.data.account.mnemonic.MnemonicHelperImpl
import com.example.votekt.data.account.mnemonic.MnemonicRepository
import com.example.votekt.data.account.mnemonic.MnemonicRepositoryImpl
import com.example.votekt.data.cache.TransactionsDatabase
import com.example.votekt.data.impl.TransactionRepositoryImpl
import com.example.votekt.data.impl.VotingRepositoryImpl
import com.example.votekt.data.local.TransactionDataSource
import com.example.votekt.ui.core.AppViewModel
import com.example.votekt.ui.create_proposal.CreateProposalViewModel
import com.example.votekt.ui.feature_create_account.ConfirmPhraseViewModel
import com.example.votekt.ui.feature_create_account.GeneratePhraseViewModel
import com.example.votekt.ui.feature_wallet.WalletViewModel
import com.example.votekt.ui.tx_history.TransactionsViewModel
import com.example.votekt.ui.voting_details.VotingDetailsViewModel
import com.example.votekt.ui.votings_list.ProposalsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val appModule = module {
    includes(web3Module)
    includes(netModule)
    includes(ethereumModule)

    viewModel { AppViewModel(
        accountRepository = get()
    ) }
    viewModel { GeneratePhraseViewModel(get()) }
    viewModel {
        ConfirmPhraseViewModel(
            mnemonicRepository = get(),
            accountRepository = get()
        )
    }
    viewModel { VotingDetailsViewModel(get()) }
    viewModel { ProposalsViewModel(get()) }
    viewModel { TransactionsViewModel(get()) }
    viewModel { CreateProposalViewModel(get()) }
    viewModel { WalletViewModel(get()) }

    single<CryptoHelper> {
        CryptoHelperImpl()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            TransactionsDatabase::class.java, "transactions.db"
        ).build()
    }

    single {
        get<TransactionsDatabase>().transactionDao()
    }

    single {
        get<TransactionsDatabase>().proposalsDao()
    }

    @Suppress("SENSELESS_COMPARISON")
    single {
        return@single if (BuildConfig.FLAVOR == "local") {
            MnemonicHelperDebugImpl()
        } else {
            MnemonicHelperImpl()
        }
    }

    single {
        KsPrefs(androidApplication().applicationContext) {
            // TODO encryption
            encryptionType = EncryptionType.PlainText()
            autoSave = AutoSavePolicy.AUTOMATIC
            commitStrategy = CommitStrategy.APPLY
        }
    }

    single<MnemonicRepository> {
        MnemonicRepositoryImpl(
            mnemonicHelper = get()
        )
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            dispatcher = Dispatchers.IO,
            ethereumClient = get(),
            cryptoHelper = get(),
            balancePollingDelay = 5.seconds,
            ksPrefs = get()
        )
    }

    single<VotingRepository> {
        VotingRepositoryImpl(get(), get(), Dispatchers.IO)
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDataSource = get(),
            ethereumClient = get(),
            dispatcher = Dispatchers.IO,
        )
    }

    single { TransactionDataSource(get()) }
}