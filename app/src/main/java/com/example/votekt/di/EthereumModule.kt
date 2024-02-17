package com.example.votekt.di

import by.alexandr7035.ethereum.core.EthereumClient
import by.alexandr7035.ethereum_impl.impl.EthereumClientImpl
import org.koin.dsl.module

val ethereumModule = module {
    single<EthereumClient> {
        EthereumClientImpl(
            api = get()
        )
    }
}