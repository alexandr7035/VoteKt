package com.example.votekt.di

import androidx.room.Room
import androidx.work.WorkManager
import by.alexandr7035.crypto.CryptoHelper
import by.alexandr7035.crypto.CryptoHelperImpl
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereumimpl.impl.EthereumEventListenerImpl
import com.cioccarellia.ksprefs.KsPrefs
import com.cioccarellia.ksprefs.config.EncryptionType
import com.cioccarellia.ksprefs.config.model.AutoSavePolicy
import com.cioccarellia.ksprefs.config.model.CommitStrategy
import com.example.votekt.BuildConfig
import com.example.votekt.data.cache.TransactionsDatabase
import com.example.votekt.data.repository.AccountRepositoryImpl
import com.example.votekt.data.repository.AppLockRepositoryImpl
import com.example.votekt.data.repository.BlockchainExplorerRepositoryImpl
import com.example.votekt.data.repository.MnemonicRepositoryImpl
import com.example.votekt.data.repository.SendTransactionRepositoryImpl
import com.example.votekt.data.repository.TransactionRepositoryImpl
import com.example.votekt.data.repository.VotingContractRepositoryImpl
import com.example.votekt.data.security.BiometricsManager
import com.example.votekt.data.security.BiometricsManagerImpl
import com.example.votekt.data.websockets.WebsocketActivityCallbacks
import com.example.votekt.data.websockets.WebsocketManagerImpl
import com.example.votekt.data.workers.AwaitTransactionWorker
import com.example.votekt.data.workers.SyncProposalsWorker
import com.example.votekt.domain.account.AccountRepository
import com.example.votekt.domain.account.MnemonicRepository
import com.example.votekt.domain.repository.BlockchainExplorerRepository
import com.example.votekt.domain.repository.DemoModeRepository
import com.example.votekt.domain.repository.DemoModeRepositoryImpl
import com.example.votekt.domain.repository.WebsocketManager
import com.example.votekt.domain.security.AppLockRepository
import com.example.votekt.domain.transactions.SendTransactionRepository
import com.example.votekt.domain.transactions.TransactionRepository
import com.example.votekt.domain.votings.VotingContractRepository
import com.example.votekt.ui.core.AppViewModel
import com.example.votekt.ui.feature.account.create.ConfirmPhraseViewModel
import com.example.votekt.ui.feature.account.create.GeneratePhraseViewModel
import com.example.votekt.ui.feature.account.restore.RestoreAccountViewModel
import com.example.votekt.ui.feature.applock.lockscreen.LockScreenViewModel
import com.example.votekt.ui.feature.applock.setup.biometrics.EnableBiometricsViewModel
import com.example.votekt.ui.feature.applock.setup.pincode.CreatePinViewModel
import com.example.votekt.ui.feature.onboarding.WelcomeScreenViewModel
import com.example.votekt.ui.feature.proposals.create.CreateProposalViewModel
import com.example.votekt.ui.feature.proposals.details.VotingDetailsViewModel
import com.example.votekt.ui.feature.proposals.feed.ProposalsViewModel
import com.example.votekt.ui.feature.transactions.history.TransactionsViewModel
import com.example.votekt.ui.feature.wallet.WalletViewModel
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

    viewModel { WelcomeScreenViewModel() }
    viewModel {
        RestoreAccountViewModel(
            getTestMnemonicUseCase = get(),
            verifyMnemonicPhraseUseCase = get(),
            addAccountUseCase = get(),
        )
    }
    viewModel {
        AppViewModel(
            accountRepository = get(),
            sendTransactionRepository = get(),
            web3EventsRepository = get(),
            checkAppLockUseCase = get(),
            connectToNodeUseCase = get(),
            getBlockchainExplorerUrlUseCase = get(),
            checkAppLockedWithBiometricsUseCase = get(),
        )
    }
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
    viewModel { CreateProposalViewModel(get(), get(), get(), get()) }
    viewModel { WalletViewModel(get(), get(), get()) }

    viewModel {
        LockScreenViewModel(
            authenticateWithPinUseCase = get(),
            checkIfBiometricsAvailableUseCase = get(),
            checkIfAppLockedWithBiometricsUseCase = get(),
            getBiometricEncryptedPinUseCase = get(),
            getBiometricDecryptionCipherUseCase = get(),
            decryptPinWithBiometricsUseCase = get(),
            logoutUseCase = get(),
        )
    }

    viewModel {
        CreatePinViewModel(setupAppLockUseCase = get(), checkIfBiometricsAvailableUseCase = get())
    }

    viewModel {
        EnableBiometricsViewModel(
            setupAppLockedWithBiometricsUseCase = get(),
            checkIfBiometricsAvailableUseCase = get()
        )
    }

    viewModel {
        EnableBiometricsViewModel(
            setupAppLockedWithBiometricsUseCase = get(),
            checkIfBiometricsAvailableUseCase = get()
        )
    }

    single<CryptoHelper> {
        CryptoHelperImpl()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            TransactionsDatabase::class.java,
            "transactions.db"
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

    single<VotingContractRepository> {
        VotingContractRepositoryImpl(
            contractAddress = BuildConfig.CONTRACT_ADDRESS,
            accountRepository = get(),
            proposalsDao = get(),
            dispatcher = Dispatchers.IO,
            sendTransactionRepository = get(),
            ethereumClient = get(),
            ksPrefs = get(),
        )
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            dispatcher = Dispatchers.IO,
            workManager = get(),
        )
    }

    single {
        SendTransactionRepositoryImpl(
            ethereumClient = get(),
            accountRepository = get(),
            transactionRepository = get(),
            proposalsDao = get(),
            ksPrefs = get(),
            cryptoHelper = get(),
        )
    } bind SendTransactionRepository::class

    single {
        EthereumEventListenerImpl(
            wssUrl = BuildConfig.ETH_WSS_NODE_URL,
            contractAddress = Address(BuildConfig.CONTRACT_ADDRESS),
            ktorClient = get()
        )
    } bind EthereumEventListener::class

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

    single<BlockchainExplorerRepository> {
        BlockchainExplorerRepositoryImpl()
    }

    single<DemoModeRepository> {
        DemoModeRepositoryImpl(androidApplication().applicationContext)
    }
}
