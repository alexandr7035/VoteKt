package com.example.votekt.core.di

import com.example.votekt.BuildConfig
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.impl.Web3RepositoryImpl
import com.example.votekt.ui.VotingViewModel
import com.example.votekt.ui.votings_list.ProposalsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

val appModule = module {
    viewModel { VotingViewModel(get()) }
    viewModel { ProposalsViewModel(get()) }

    single<Web3Repository> {
        Web3RepositoryImpl(get(), Dispatchers.IO)
    }

    single<Web3j> {
        Web3j.build(HttpService(BuildConfig.ETH_NODE_URL))
    }
}