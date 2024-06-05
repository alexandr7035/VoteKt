package by.alexandr7035.ethereum_impl.impl

import android.util.Log
import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.eth_events.EthEventsSubscriptionState
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import by.alexandr7035.ethereum_impl.model.EthWebsocketEventRaw
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import by.alexandr7035.utils.removeHexPrefix
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.kethereum.model.Address

class EthereumEventListenerImpl(
    private val wssUrl: String,
    private val contractAddress: Address,
    private val ktorClient: HttpClient,
) : EthereumEventListener {
    private val wssStatusFlow = MutableStateFlow<EthEventsSubscriptionState>(EthEventsSubscriptionState.Connecting)

    private val moshi = Moshi.Builder().build()
    private val requestAdapter = moshi.adapter(JsonRpcRequest::class.java)
    private val responseAdapter = moshi.adapter(EthWebsocketEventRaw::class.java)

    private var session: DefaultClientWebSocketSession? = null

    override fun subscriptionStateFlow(): Flow<EthEventsSubscriptionState> {
        return wssStatusFlow
    }

    override suspend fun subscribeToEthereumEvents(): Flow<EthereumEvent> = flow {
        try {
            wssStatusFlow.emit(EthEventsSubscriptionState.Connecting)
            Log.d(LOG_TAG, "establishing websocket connection")

            session = ktorClient.webSocketSession() {
                url(wssUrl)
            }

            session!!.incoming
                .consumeAsFlow()
                .onStart {
                    session!!.outgoing.send(getSubscribeToContractRequest(contractAddress))
                    wssStatusFlow.emit(EthEventsSubscriptionState.Connected)
                }
                .filterIsInstance<Frame.Text>()
                .onEach {
                    Log.d(LOG_TAG, "raw websocket event ${it.readText()}")
                    emit(parseWssEvent(it.readText()))
                }
                .collect()
        } catch (e: Exception) {
            reduceWssDisconnected(e)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun disconnect() {
        session?.close()
        session = null
        reduceWssDisconnected(null)
    }

    private suspend fun reduceWssDisconnected(e: Throwable?) {
        Log.d(LOG_TAG, "websocket disconnected with error ${e} ${e?.message}")
        wssStatusFlow.emit(EthEventsSubscriptionState.Disconnected)
    }

    private fun parseWssEvent(rawEvent: String): EthereumEvent {
        try {
            val res = responseAdapter.fromJson(rawEvent)
            Log.d(LOG_TAG, "incoming websocket event: ${res?.params?.result}")

            res?.params?.result?.let {
                return EthereumEvent.ContractEvent(
                    eventTopic = it.topics.first().removeHexPrefix(),
                    encodedData = it.data,
                )
            }
        } catch (e: Exception) {
            Log.d(LOG_TAG, "failed to handle raw event, skip: $rawEvent")
        }

        return EthereumEvent.UnknownEvent
    }

    private fun getSubscribeToContractRequest(contractAddress: Address): Frame {
        val raw = requestAdapter.toJson(
            JsonRpcRequest(
                method = EthNodeMethods.FUNCTION_SUBSCRIBE,
                params = listOf(KEY_LOGS, mapOf(KEY_ADDRESS to contractAddress.hex))
            )
        )

        Log.d(LOG_TAG, "establishing websockets with ${raw}")

        return Frame.Text(raw)
    }

    private companion object {
        private const val LOG_TAG = "WSS_TAG"
        private const val KEY_LOGS = "logs"
        private const val KEY_ADDRESS = "address"
    }
}
