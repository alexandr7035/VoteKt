package com.example.votekt.core

import android.app.Application
import com.example.votekt.data.websockets.WebsocketActivityCallbacks
import com.example.votekt.di.appModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class VoteKtApp : Application() {

    private val activityCallbacks: WebsocketActivityCallbacks by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VoteKtApp)
            workManagerFactory()
            modules(appModule)
        }

        registerActivityLifecycleCallbacks(activityCallbacks)
    }
}
