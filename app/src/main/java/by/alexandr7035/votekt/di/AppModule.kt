package by.alexandr7035.votekt.di

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
import by.alexandr7035.votekt.BuildConfig
import by.alexandr7035.votekt.data.cache.TransactionsDatabase
import by.alexandr7035.votekt.data.repository.AccountRepositoryImpl
import by.alexandr7035.votekt.data.repository.AppLockRepositoryImpl
import by.alexandr7035.votekt.data.repository.BlockchainExplorerRepositoryImpl
import by.alexandr7035.votekt.data.repository.MnemonicRepositoryImpl
import by.alexandr7035.votekt.data.repository.SendTransactionRepositoryImpl
import by.alexandr7035.votekt.data.repository.TransactionRepositoryImpl
import by.alexandr7035.votekt.data.repository.VotingContractRepositoryImpl
import by.alexandr7035.votekt.data.security.BiometricsManager
import by.alexandr7035.votekt.data.security.BiometricsManagerImpl
import by.alexandr7035.votekt.data.websockets.WebsocketActivityCallbacks
import by.alexandr7035.votekt.data.websockets.WebsocketManagerImpl
import by.alexandr7035.votekt.data.workers.AwaitTransactionWorker
import by.alexandr7035.votekt.data.workers.SyncProposalsWorker
import by.alexandr7035.votekt.domain.account.AccountRepository
import by.alexandr7035.votekt.domain.account.MnemonicRepository
import by.alexandr7035.votekt.domain.repository.BlockchainExplorerRepository
import by.alexandr7035.votekt.domain.repository.DemoModeRepository
import by.alexandr7035.votekt.domain.repository.DemoModeRepositoryImpl
import by.alexandr7035.votekt.domain.repository.WebsocketManager
import by.alexandr7035.votekt.domain.security.AppLockRepository
import by.alexandr7035.votekt.domain.transactions.SendTransactionRepository
import by.alexandr7035.votekt.domain.transactions.TransactionRepository
import by.alexandr7035.votekt.domain.votings.VotingContractRepository
import by.alexandr7035.votekt.ui.core.AppViewModel
import by.alexandr7035.votekt.ui.feature.account.create.ConfirmPhraseViewModel
import by.alexandr7035.votekt.ui.feature.account.create.GeneratePhraseViewModel
import by.alexandr7035.votekt.ui.feature.account.restore.RestoreAccountViewModel
import by.alexandr7035.votekt.ui.feature.applock.lockscreen.LockScreenViewModel
import by.alexandr7035.votekt.ui.feature.applock.setup.biometrics.EnableBiometricsViewModel
import by.alexandr7035.votekt.ui.feature.applock.setup.pincode.CreatePinViewModel
import by.alexandr7035.votekt.ui.feature.onboarding.WelcomeScreenViewModel
import by.alexandr7035.votekt.ui.feature.proposals.create.CreateProposalViewModel
import by.alexandr7035.votekt.ui.feature.proposals.details.VotingDetailsViewModel
import by.alexandr7035.votekt.ui.feature.proposals.feed.ProposalsViewModel
import by.alexandr7035.votekt.ui.feature.transactions.history.TransactionsViewModel
import by.alexandr7035.votekt.ui.feature.wallet.WalletViewModel
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
