package com.example.votekt.core

import android.app.Application
import com.example.votekt.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class VoteKtApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VoteKtApp)
            modules(appModule)
        }
    }
}