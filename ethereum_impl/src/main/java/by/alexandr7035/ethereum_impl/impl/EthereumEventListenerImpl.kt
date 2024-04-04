package by.alexandr7035.ethereum_impl.impl

import by.alexandr7035.ethereum.core.EthereumEventListener
import by.alexandr7035.ethereum.model.eth_events.EthereumEvent
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import by.alexandr7035.utils.addHexPrefix
import by.alexandr7035.utils.removeHexPrefix
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import okhttp3.OkHttp
import org.kethereum.model.Address

class EthereumEventListenerImpl(
    private val wssUrl: String,
    private val contractAddress: Address,
): EthereumEventListener {
    val client = HttpClient() {
        engine { OkHttp }
        install(WebSockets)
    }

    val wssStatusFlow = MutableStateFlow<WebsocketsStatus>(WebsocketsStatus.Connecting)

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val moshi = Moshi.Builder().build()
    val requestAdapter = moshi.adapter(JsonRpcRequest::class.java)
    val responseAdapter = moshi.adapter(WssResult::class.java)

    override suspend fun subscribe(): Flow<EthereumEvent> = flow {
        try {
            // TODO request model
            val subscribeRequest = JsonRpcRequest(
                method = "eth_subscribe",
                params = listOf("logs", mapOf(
                    "address" to contractAddress.hex
                ))
            )

            client.webSocket(
                method = HttpMethod.Get,
                host = wssUrl,
                path = DEFAULT_WSS_PATH,
            ) {
                wssStatusFlow.emit(WebsocketsStatus.Active)
                outgoing.send(Frame.Text(requestAdapter.toJson(subscribeRequest)))

                while(coroutineScope.isActive) {
                    val othersMessage = incoming.receive() as? Frame.Text
                    othersMessage?.let {
                        emit(parseWssEvent(it.readText()))
                    }
                }
            }

        } catch (e: ServerResponseException) {
            wssStatusFlow.emit(WebsocketsStatus.Disconnected)
            println("Ktor Error getting remote items: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            wssStatusFlow.emit(WebsocketsStatus.Disconnected)
            println("Ktor Error getting remote items: ${e.response.status.description}")
        } catch (e: RedirectResponseException) {
            wssStatusFlow.emit(WebsocketsStatus.Disconnected)
            println("KtorError getting remote items: ${e.response.status.description}")
        }
        catch (e: Exception) {
            wssStatusFlow.emit(WebsocketsStatus.Disconnected)
            println("KtorError getting remote items: ${e.localizedMessage}")
        }
    }


    private fun parseWssEvent(rawEvent: String): EthereumEvent {
        try {
            val res = responseAdapter.fromJson(rawEvent)
            println("Contract event?: ${res?.params?.result}")

            res?.params?.result?.let {
                return EthereumEvent.ContractEvent(
                    eventTopic = it.topics.first().removeHexPrefix(),
                    encodedData = it.data,
                )
            }
        }
        catch (e: Exception) {
            println("failed to handle raw event ${rawEvent}")
        }

        return EthereumEvent.UnknownEvent
    }

    private companion object {
        private const val DEFAULT_WSS_PATH = "/"
    }
}


sealed class WebsocketsStatus {
    object Connecting: WebsocketsStatus()
    object Active: WebsocketsStatus()
    object Disconnected: WebsocketsStatus()
}

@JsonClass(generateAdapter = true)
data class WssResult(
    @Json(name = "jsonrpc") val jsonRpc: String,
    @Json(name = "params") val params: WssParams?
)

@JsonClass(generateAdapter = true)
data class WssParams(
    @Json(name = "subscription") val subscription: String,
    @Json(name = "result") val result: WssContractResult
)

@JsonClass(generateAdapter = true)
data class WssContractResult(
    @Json(name = "data") val data: String,
    @Json(name = "address") val address: String,
    @Json(name = "topics") val topics: List<String>,
)
