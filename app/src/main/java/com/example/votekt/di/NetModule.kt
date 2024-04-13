package com.example.votekt.di

import by.alexandr7035.ethereum_impl.adapters.AddressAdapter
import by.alexandr7035.ethereum_impl.adapters.BigDecimalNumberAdapter
import by.alexandr7035.ethereum_impl.adapters.DecimalNumberAdapter
import by.alexandr7035.ethereum_impl.adapters.DefaultNumberAdapter
import by.alexandr7035.ethereum_impl.adapters.HexNumberAdapter
import by.alexandr7035.ethereum_impl.adapters.WeiAdapter
import by.alexandr7035.ethereum_impl.api.RetrofitEthereumRpcApi
import com.example.votekt.BuildConfig
import com.example.votekt.data.InfuraAuthInterceptor
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val INFURA_TEST_FLAVOR = "infuraTestnet"

val netModule = module {
    single {
        val builder = OkHttpClient()
            .newBuilder().apply {
                if (BuildConfig.FLAVOR == INFURA_TEST_FLAVOR) {
                    addInterceptor(InfuraAuthInterceptor(BuildConfig.INFURA_API_KEY))
                }
            }

        builder.build()
    }

    // Ktor
    single {
        HttpClient(OkHttp) {
            engine {
                preconfigured = OkHttpClient.Builder()
                    .pingInterval(30, TimeUnit.SECONDS)
                    .build()
            }
            install(WebSockets)
        }
    }

    single<Moshi> {
        Moshi.Builder()
            .add(WeiAdapter())
            .add(AddressAdapter())
            // Number adapters taken from Gnosis
            .add(DecimalNumberAdapter())
            .add(BigDecimalNumberAdapter())
            .add(HexNumberAdapter())
            .add(DefaultNumberAdapter())
            .build()
    }

    single<RetrofitEthereumRpcApi> {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .baseUrl(BuildConfig.ETH_NODE_URL)
            .build()
            .create(RetrofitEthereumRpcApi::class.java)
    }
    
}