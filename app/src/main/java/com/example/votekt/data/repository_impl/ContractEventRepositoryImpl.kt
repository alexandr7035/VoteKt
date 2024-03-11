package com.example.votekt.data.repository_impl

import android.util.Log
import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum_impl.impl.ContractEventListener
import com.example.votekt.BuildConfig
import com.example.votekt.domain.account.ContractEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

class ContractEventRepositoryImpl(
    wssUrl: String,
): ContractEventRepository {
    private val listener = ContractEventListener(wssUrl)

    override suspend fun subscribe() {
        listener.subscribe(
            contractAddress = Address(BuildConfig.CONTRACT_ADDRESS)
        )
        .onEach {
            // TODO process events
            Log.d(TAG, "new WSS event ${it}")
        }
        .flowOn(Dispatchers.IO)
        .collect()
    }

    companion object {
        private const val TAG = "WSS_TAG"
    }
}
