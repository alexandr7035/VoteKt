package com.example.votekt.di

import com.example.votekt.BuildConfig
import org.koin.dsl.module
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

val web3Module = module {
    single<Web3j> {
        Web3j.build(HttpService(BuildConfig.ETH_NODE_URL))
    }
}