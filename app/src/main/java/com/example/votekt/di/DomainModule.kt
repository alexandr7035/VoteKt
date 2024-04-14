package com.example.votekt.di

import com.example.votekt.domain.usecase.account.GetTestMnemonicUseCase
import com.example.votekt.domain.datasync.SyncWithContractUseCase
import com.example.votekt.domain.usecase.account.AddAccountUseCase
import com.example.votekt.domain.usecase.account.GenerateAccountUseCase
import com.example.votekt.domain.usecase.account.VerifyMnemonicPhraseUseCase
import org.koin.dsl.module

val domainModule = module {
    single { GenerateAccountUseCase(get()) }
    single { GetTestMnemonicUseCase(get()) }
    single { AddAccountUseCase(get()) }
    single { VerifyMnemonicPhraseUseCase(get()) }

    single { SyncWithContractUseCase(get()) }
}