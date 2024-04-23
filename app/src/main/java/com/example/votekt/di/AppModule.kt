package com.example.votekt.di

import androidx.room.Room
import androidx.work.WorkManager
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.crypto.CryptoHelperImpl
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum_impl.impl.EthereumEventListenerImpl
import by.alexandr7035.ethereum_impl.impl.NodeWssConfiguration
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.example.votekt.BuildConfig
import com.example.votekt.data.cache.TransactionsDatabase
import com.example.votekt.data.repository_impl.AccountRepositoryImpl
import com.example.votekt.data.repository_impl.AppLockRepositoryImpl
import com.example.votekt.data.repository_impl.MnemonicRepositoryImpl
import com.example.votekt.data.repository_impl.SendTransactionRepositoryImpl
import com.example.votekt.data.repository_impl.TransactionRepositoryImpl
import com.example.votekt.data.repository_impl.VotingContractRepositoryImpl
import com.example.votekt.data.security.BiometricsManager
import com.example.votekt.data.security.BiometricsManagerImpl
import com.example.votekt.data.websockets.WebsocketActivityCallbacks
import com.example.votekt.data.websockets.WebsocketManagerImpl
import com.example.votekt.data.workers.AwaitTransactionWorker
import com.example.votekt.data.workers.SyncProposalsWorker
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.account.MnemonicRepository
import com.example.votekt.domain.repository.WebsocketManager
import com.example.votekt.domain.security.AppLockRepository
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.core.AppViewModel
import com.example.votekt.ui.create_proposal.CreateProposalViewModel
import com.example.votekt.ui.feature_app_lock.lock_screen.LockScreenViewModel
import com.example.votekt.ui.feature_app_lock.setup_applock.biometrics.EnableBiometricsViewModel
import com.example.votekt.ui.feature_app_lock.setup_applock.create_pin.CreatePinViewModel
import com.example.votekt.ui.feature_create_account.ConfirmPhraseViewModel
import com.example.votekt.ui.feature_create_account.GeneratePhraseViewModel
import com.example.votekt.ui.feature_proposals.proposal_details.VotingDetailsViewModel
import com.example.votekt.ui.feature_proposals.proposals_list.ProposalsViewModel
import com.example.votekt.ui.feature_restore_account.RestoreAccountViewModel
import com.example.votekt.ui.feature_wallet.WalletViewModel
import com.example.votekt.ui.feature_welcome.WelcomeScreenViewModel
import com.example.votekt.ui.tx_history.TransactionsViewModel
import kotlinx.coroutines.Dispatchers
import org.kethereum.model.Address
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val appModule = module {
    includes(netModule)
    includes(ethereumModule)
    includes(domainModule)

    viewModel { WelcomeScreenViewModel(
    ) }
    viewModel { RestoreAccountViewModel(
        getTestMnemonicUseCase = get(),
        verifyMnemonicPhraseUseCase = get(),
        addAccountUseCase = get(),
    ) }
    viewModel { AppViewModel(
        accountRepository = get(),
        sendTransactionRepository = get(),
        web3EventsRepository = get(),
        checkAppLockUseCase = get(),
        connectToNodeUseCase = get(),
    ) }
    viewModel { GeneratePhraseViewModel(get()) }
    viewModel {
        ConfirmPhraseViewModel(
            mnemonicRepository = get(),
            accountRepository = get()
        )
    }
    viewModel { VotingDetailsViewModel(get()) }
    viewModel { ProposalsViewModel(get(), get()) }
    viewModel { TransactionsViewModel(get()) }
    viewModel { CreateProposalViewModel(get()) }
    viewModel { WalletViewModel(get(), get()) }

    viewModel { LockScreenViewModel(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
    ) }

    viewModel {
        CreatePinViewModel(get(), get())
    }

    viewModel {
        EnableBiometricsViewModel(get(), get())
    }

    viewModel { EnableBiometricsViewModel(get(), get()) }

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

    single {
        get<TransactionsDatabase>().proposalsDao()
    }

    single {
        KsPrefs(androidApplication().applicationContext) {

            encryptionType = EncryptionType.KeyStore(
                alias = "shared_prefs_keystore_alias",
            )
            autoSave = AutoSavePolicy.AUTOMATIC
            commitStrategy = CommitStrategy.APPLY
        }
    }

    single {
        WorkManager.getInstance(androidApplication().applicationContext)
    }

    worker {
        AwaitTransactionWorker(
            appContext = get(),
            params = get(),
            transactionRepository = get(),
            ethereumClient = get()
        )
    }

    worker {
        SyncProposalsWorker(
            appContext = get(),
            params = get(),
            votingContractRepository = get(),
        )
    }

    single<MnemonicRepository> {
        MnemonicRepositoryImpl()
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            dispatcher = Dispatchers.IO,
            ethereumClient = get(),
            cryptoHelper = get(),
            balancePollingDelay = 20.seconds,
            ksPrefs = get()
        )
    }

    // TODO dispatcher annotation
    single<VotingContractRepository> {
        VotingContractRepositoryImpl(
            contractAddress = BuildConfig.CONTRACT_ADDRESS,
            accountRepository = get(),
            proposalsDao = get(),
            dispatcher = Dispatchers.IO,
            sendTransactionRepository = get(),
            web3 = get()
        )
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            dispatcher = Dispatchers.IO,
            workManager = get(),
        )
    }

    single { SendTransactionRepositoryImpl(
        ethereumClient = get(),
        accountRepository = get(),
        transactionRepository = get(),
        proposalsDao = get(),
        ksPrefs = get(),
        cryptoHelper = get(),
    ) } bind SendTransactionRepository::class

    single { EthereumEventListenerImpl(
        wssUrl = BuildConfig.ETH_WSS_NODE_URL,
        contractAddress = Address(BuildConfig.CONTRACT_ADDRESS),
        ktorClient = get(),
        wssConfiguration = if (BuildConfig.FLAVOR == "local") {
            NodeWssConfiguration.CLEARTEXT
        } else {
            NodeWssConfiguration.WSS
        }
    ) } bind EthereumEventListener::class

    single { BiometricsManagerImpl() } bind BiometricsManager::class

    single<AppLockRepository> {
        val context = androidApplication().applicationContext

        AppLockRepositoryImpl(
            context = context,
            ksPrefs = get(),
            biometricsManager = get(),
            moshi = get(),
        )
    }

    single {
        WebsocketActivityCallbacks(
            websocketManager = get(),
        )
    }

    single<WebsocketManager> {
        WebsocketManagerImpl(
            ethereumEventListener = get(),
            votingContractRepository = get(),
        )
    }
}