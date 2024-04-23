package com.example.votekt.data.websockets

import android.util.Log
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.domain.repository.WebsocketManager
import com.example.votekt.domain.votings.VotingContractRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WebsocketManagerImpl(
    private val ethereumEventListener: EthereumEventListener,
    private val votingContractRepository: VotingContractRepository,
): WebsocketManager {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "uncaught exception $throwable")
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + exceptionHandler)

    override fun connect() {
        coroutineScope.launch {
            subscribeToWssEvents()
        }
    }

    override fun disconnect() {
        coroutineScope.launch {
            ethereumEventListener.disconnect()
        }
    }

    private suspend fun subscribeToWssEvents() {
        ethereumEventListener
            .subscribeToEthereumEvents()
            .onStart {
                val syncResult = OperationResult.runWrapped {
                    votingContractRepository.syncProposalsWithContract()
                }

                when (syncResult) {
                    is OperationResult.Success -> Log.d(TAG,"data synced with contract")
                    is OperationResult.Failure -> throw syncResult.error
                }
            }
            .onEach {
                if (it is EthereumEvent.ContractEvent) {
                    votingContractRepository.handleContractEvent(it)
                }
            }
            .flowOn(Dispatchers.IO)
            .catch {
                Log.d(TAG, "error in subscription ${it}")
                disconnect()
            }
            .launchIn(coroutineScope)
    }


    companion object {
        private val TAG = this::class.simpleName
    }
}