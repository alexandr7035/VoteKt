package by.alexandr7035.votekt.di

import by.alexandr7035.votekt.domain.datasync.SyncWithContractUseCase
import by.alexandr7035.votekt.domain.security.AuthenticateWithPinUseCase
import by.alexandr7035.votekt.domain.security.CheckAppLockUseCase
import by.alexandr7035.votekt.domain.security.CheckAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.security.CheckIfBiometricsAvailableUseCase
import by.alexandr7035.votekt.domain.security.DecryptPinWithBiometricsUseCase
import by.alexandr7035.votekt.domain.security.GetBiometricDecryptionCipherUseCase
import by.alexandr7035.votekt.domain.security.GetBiometricEncryptedPinUseCase
import by.alexandr7035.votekt.domain.security.SetupAppLockUseCase
import by.alexandr7035.votekt.domain.security.SetupAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.account.AddAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.GenerateAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.LogoutUseCase
import by.alexandr7035.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import by.alexandr7035.votekt.domain.usecase.contract.CreateDraftProposalUseCase
import by.alexandr7035.votekt.domain.usecase.contract.GetContractConfigurationUseCase
import by.alexandr7035.votekt.domain.usecase.contract.GetContractStateUseCase
import by.alexandr7035.votekt.domain.usecase.debug.GetTestMnemonicUseCase
import by.alexandr7035.votekt.domain.usecase.demo.GetDemoProposalUseCase
import by.alexandr7035.votekt.domain.usecase.demo.IsDemoModeEnabledUseCase
import by.alexandr7035.votekt.domain.usecase.explorer.GetBlockchainExplorerUrlUseCase
import by.alexandr7035.votekt.domain.usecase.node.ConnectToNodeUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GenerateAccountUseCase(get()) }
    single { GetTestMnemonicUseCase(get()) }
    single { AddAccountUseCase(get()) }
    single { VerifyMnemonicPhraseUseCase(get()) }
    single { LogoutUseCase(get(), get(), get(), get()) }

    single { SyncWithContractUseCase(get()) }

    single { ConnectToNodeUseCase(get()) }

    single { SetupAppLockUseCase(get()) }
    single { SetupAppLockedWithBiometricsUseCase(get()) }
    single { CheckIfBiometricsAvailableUseCase(get()) }
    single { CheckAppLockedWithBiometricsUseCase(get()) }
    single { CheckAppLockedWithBiometricsUseCase(get()) }
    single { CheckAppLockUseCase(get()) }
    single { AuthenticateWithPinUseCase(get()) }

    single { GetBiometricEncryptedPinUseCase(get()) }
    single { GetBiometricDecryptionCipherUseCase(get()) }
    single { DecryptPinWithBiometricsUseCase(get()) }

    single { CreateDraftProposalUseCase(get()) }
    single { GetContractStateUseCase(get()) }
    single { GetContractConfigurationUseCase(get()) }

    single { GetBlockchainExplorerUrlUseCase(get()) }

    single { IsDemoModeEnabledUseCase(get()) }
    single { GetDemoProposalUseCase(get()) }
}
