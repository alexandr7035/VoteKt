package com.example.votekt.core.di

import com.example.votekt.ui.VotingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { VotingViewModel() }
}