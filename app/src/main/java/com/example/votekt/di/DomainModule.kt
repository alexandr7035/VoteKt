package com.example.votekt.di

import com.example.votekt.domain.datasync.SyncWithContractUseCase
import com.example.votekt.domain.security.AuthenticateWithPinUseCase
import com.example.votekt.domain.security.CheckAppLockUseCase
import com.example.votekt.domain.security.CheckAppLockedWithBiometricsUseCase
import com.example.votekt.domain.security.CheckIfBiometricsAvailableUseCase
import com.example.votekt.domain.security.DecryptPinWithBiometricsUseCase
import com.example.votekt.domain.security.GetBiometricDecryptionCipherUseCase
import com.example.votekt.domain.security.GetBiometricEncryptedPinUseCase
import com.example.votekt.domain.security.SetupAppLockUseCase
import com.example.votekt.domain.security.SetupAppLockedWithBiometricsUseCase
import com.example.votekt.domain.usecase.account.AddAccountUseCase
import com.example.votekt.domain.usecase.account.GenerateAccountUseCase
import com.example.votekt.domain.usecase.account.LogoutUseCase
import com.example.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import com.example.votekt.domain.usecase.contract.CreateDraftProposalUseCase
import com.example.votekt.domain.usecase.contract.GetContractConfigurationUseCase
import com.example.votekt.domain.usecase.contract.GetContractStateUseCase
import com.example.votekt.domain.usecase.debug.GetTestMnemonicUseCase
import com.example.votekt.domain.usecase.demo.GetDemoProposalUseCase
import com.example.votekt.domain.usecase.demo.IsDemoModeEnabledUseCase
import com.example.votekt.domain.usecase.explorer.GetBlockchainExplorerUrlUseCase
import com.example.votekt.domain.usecase.node.ConnectToNodeUseCase
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
