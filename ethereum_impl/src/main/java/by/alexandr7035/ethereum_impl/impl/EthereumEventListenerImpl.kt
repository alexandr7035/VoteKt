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
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import org.kethereum.model.Address

class EthereumEventListenerImpl(
    private val wssUrl: String,
    private val contractAddress: Address,
    private val ktorClient: HttpClient,
    private val wssConfiguration: NodeWssConfiguration,
): EthereumEventListener {
    private val wssStatusFlow = MutableStateFlow<EthEventsSubscriptionState>(EthEventsSubscriptionState.Connecting)

    private val moshi = Moshi.Builder().build()
    private val requestAdapter = moshi.adapter(JsonRpcRequest::class.java)
    private val responseAdapter = moshi.adapter(EthWebsocketEventRaw::class.java)

    override fun subscriptionStateFlow(): Flow<EthEventsSubscriptionState> {
        return wssStatusFlow
    }

    override suspend fun subscribeToEthereumEvents(): Flow<EthereumEvent> = flow {
        wssStatusFlow.emit(EthEventsSubscriptionState.Connecting)
        Log.d(LOG_TAG, "establishing websocket connection")

        try {
            ktorClient.webSocket(
                method = HttpMethod.Get,
                host = wssUrl,
                request = getRequestBuilder()
            ) {
                wssStatusFlow.emit(EthEventsSubscriptionState.Connected)
                outgoing.send(getSubscribeToContractRequest(contractAddress))

                try {
                    while(isActive) {
                        when (val frame = incoming.receive()) {
                            is Frame.Ping -> {
                                send(getPongRequest(frame))
                            }
                            is Frame.Text -> {
                                emit(parseWssEvent(frame.readText()))
                            }
                            else -> {}
                        }
                    }
                } catch (e: Exception) {
                    reduceWssDisconnected(e)
                }
            }

        } catch (e: Exception) {
            reduceWssDisconnected(e)
        }
    }.flowOn(Dispatchers.IO)

    private fun getRequestBuilder(): HttpRequestBuilder.() -> Unit = when (wssConfiguration) {
        NodeWssConfiguration.CLEARTEXT -> {
            {}
        }

        NodeWssConfiguration.WSS -> {
            {
                url.protocol = URLProtocol.WSS
                url.port = url.protocol.defaultPort
            }
        }
    }

    override suspend fun disconnect() {
        try {
            ktorClient.webSocket(
                method = HttpMethod.Get,
                host = wssUrl,
                request = getRequestBuilder()
            ) {
                outgoing.send(Frame.Close())
            }
        } catch (e: Exception) {
            reduceWssDisconnected(e)
        }
        reduceWssDisconnected(null)
    }

    private suspend fun reduceWssDisconnected(e: Exception?) {
        Log.d(LOG_TAG,"websocket disconnected with error ${e} ${e?.message}")
        wssStatusFlow.emit(EthEventsSubscriptionState.Disconnected)
    }

    private fun parseWssEvent(rawEvent: String): EthereumEvent {
        try {
            val res = responseAdapter.fromJson(rawEvent)
            Log.d(LOG_TAG, "Contract event?: ${res?.params?.result}")

            res?.params?.result?.let {
                return EthereumEvent.ContractEvent(
                    eventTopic = it.topics.first().removeHexPrefix(),
                    encodedData = it.data,
                )
            }
        }
        catch (e: Exception) {
            Log.d(LOG_TAG, "failed to handle raw event $rawEvent")
        }

        return EthereumEvent.UnknownEvent
    }

    private fun getSubscribeToContractRequest(contractAddress: Address): Frame {
        val raw = requestAdapter.toJson(
            JsonRpcRequest(
                method = EthNodeMethods.FUNCTION_SUBSCRIBE,
                params = listOf("logs", mapOf(
                    "address" to contractAddress.hex
                ))
            )
        )

        return Frame.Text(raw)
    }

    private fun getPongRequest(ping: Frame.Ping): Frame {
        return Frame.Pong(ping.buffer)
    }

    private companion object {
        private const val LOG_TAG = "WSS_TAG"
    }
}
