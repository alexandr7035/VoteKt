package com.example.votekt.di

import by.alexandr7035.ethereum.core.EthereumRepository
import by.alexandr7035.ethereum_impl.impl.EthereumRepositoryImpl
import org.koin.dsl.module

val ethereumModule = module {
    single<EthereumRepository> {
        EthereumRepositoryImpl(
            api = get()
        )
    }
}