package com.example.votekt.di

import com.example.votekt.BuildConfig
import com.example.votekt.data.web3_core.transactions.processor.AwaitingTxProcessor
import com.example.votekt.data.web3_core.transactions.processor.TxProcessor
import org.koin.dsl.module
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import kotlin.time.Duration.Companion.seconds

val web3Module = module {
    single<Web3j> {
        Web3j.build(HttpService(BuildConfig.ETH_NODE_URL))
    }

    single<TxProcessor> {
        AwaitingTxProcessor(
            web3j = get(),
            maxWaitingTime = 10.seconds,
        )
    }
}