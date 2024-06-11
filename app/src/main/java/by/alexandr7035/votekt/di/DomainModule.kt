package by.alexandr7035.votekt.di

import by.alexandr7035.votekt.domain.usecase.contract.SyncWithContractUseCase
import by.alexandr7035.votekt.domain.usecase.applock.AuthenticateWithPinUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckAppLockUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.applock.CheckIfBiometricsAvailableUseCase
import by.alexandr7035.votekt.domain.usecase.applock.DecryptPinWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.applock.GetBiometricDecryptionCipherUseCase
import by.alexandr7035.votekt.domain.usecase.applock.GetBiometricEncryptedPinUseCase
import by.alexandr7035.votekt.domain.usecase.applock.SetupAppLockUseCase
import by.alexandr7035.votekt.domain.usecase.applock.SetupAppLockedWithBiometricsUseCase
import by.alexandr7035.votekt.domain.usecase.account.AddAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.CheckAccountCreatedUseCase
import by.alexandr7035.votekt.domain.usecase.account.ConfirmNewAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.GenerateAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.GetAccountConfirmationData
import by.alexandr7035.votekt.domain.usecase.account.GetSelfAccountUseCase
import by.alexandr7035.votekt.domain.usecase.account.LogoutUseCase
import by.alexandr7035.votekt.domain.usecase.account.ObserveBalanceUseCase
import by.alexandr7035.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import by.alexandr7035.votekt.domain.usecase.contract.CreateDraftProposalUseCase
import by.alexandr7035.votekt.domain.usecase.contract.GetContractConfigurationUseCase
import by.alexandr7035.votekt.domain.usecase.contract.GetContractStateUseCase
import by.alexandr7035.votekt.domain.usecase.debug.GetTestMnemonicUseCase
import by.alexandr7035.votekt.domain.usecase.demo.GetDemoProposalUseCase
import by.alexandr7035.votekt.domain.usecase.demo.IsDemoModeEnabledUseCase
import by.alexandr7035.votekt.domain.usecase.explorer.GetBlockchainExplorerUrlUseCase
import by.alexandr7035.votekt.domain.usecase.node.ConnectToNodeUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.DeleteDraftProposalUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.DeployDraftProposalUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.ObserveProposalUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.ObserveProposalsUseCase
import by.alexandr7035.votekt.domain.usecase.proposal.VoteOnProposalUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.CancelOutgoingTransactionUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ClearTransactionsUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ConfirmOutgoingTransactionUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ObserveOutgoingTransactionUseCase
import by.alexandr7035.votekt.domain.usecase.transactions.ObserveTransactionsUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GenerateAccountUseCase(get()) }
    single { GetTestMnemonicUseCase(get()) }
    single { AddAccountUseCase(get()) }
    single { GetAccountConfirmationData(get()) }
    single { ConfirmNewAccountUseCase(get()) }
    single { CheckAccountCreatedUseCase(get()) }
    single { GetSelfAccountUseCase(get()) }
    single { VerifyMnemonicPhraseUseCase(get()) }
    single { LogoutUseCase(get(), get(), get(), get()) }

    single { ObserveBalanceUseCase(get()) }

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
    single { DeleteDraftProposalUseCase(get()) }
    single { DeployDraftProposalUseCase(get()) }
    single { GetContractStateUseCase(get()) }
    single { GetContractConfigurationUseCase(get()) }
    single { VoteOnProposalUseCase(get()) }
    single { ObserveProposalsUseCase(get()) }
    single { ObserveProposalUseCase(get()) }

    single { GetBlockchainExplorerUrlUseCase(get()) }

    single { IsDemoModeEnabledUseCase(get()) }
    single { GetDemoProposalUseCase(get()) }

    single { ObserveTransactionsUseCase(get()) }
    single { ClearTransactionsUseCase(get()) }
    single { ObserveOutgoingTransactionUseCase(get()) }
    single { ConfirmOutgoingTransactionUseCase(get()) }
    single { CancelOutgoingTransactionUseCase(get()) }
}
