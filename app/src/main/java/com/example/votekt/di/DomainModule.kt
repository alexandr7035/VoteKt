package com.example.votekt.di

import com.example.votekt.domain.datasync.SyncWithContractUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SyncWithContractUseCase(get()) }
}