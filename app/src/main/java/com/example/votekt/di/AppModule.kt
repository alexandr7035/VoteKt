package com.example.votekt.di

import androidx.room.Room
import com.example.votekt.data.TransactionRepository
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.cache.TransactionsDatabase
import com.example.votekt.data.impl.TransactionRepositoryImpl
import com.example.votekt.data.impl.Web3RepositoryImpl
import com.example.votekt.ui.create_proposal.CreateProposalViewModel
import com.example.votekt.ui.tx_history.TransactionsViewModel
import com.example.votekt.ui.voting_details.VotingDetailsViewModel
import com.example.votekt.ui.votings_list.ProposalsViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(web3Module)

    viewModel { VotingDetailsViewModel(get()) }
    viewModel { ProposalsViewModel(get()) }
    viewModel { TransactionsViewModel(get()) }
    viewModel { CreateProposalViewModel(get()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            TransactionsDatabase::class.java, "transactions.db"
        ).build()
    }

    single {
        get<TransactionsDatabase>().transactionDao()
    }

    single<Web3Repository> {
        Web3RepositoryImpl(get(), get(), Dispatchers.IO)
    }

    single<TransactionRepository> {
        TransactionRepositoryImpl(get(), get(), Dispatchers.IO)
    }
}