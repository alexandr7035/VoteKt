package by.alexandr7035.ethereum_impl.impl

import by.alexandr7035.ethereum.model.Address
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

val client = HttpClient() {
    engine { OkHttp }
    install(WebSockets)
}

class ContractEventListener(
   private val wssUrl: String
) {

    suspend fun subscribe(contractAddress: Address): Flow<String> = flow {
        try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(JsonRpcRequest::class.java)

            // TODO request model
            val subscribeRequest = JsonRpcRequest(
                method = "eth_subscribe",
                params = listOf("logs", mapOf(
                    "address" to contractAddress.value
                ))
            )

            client.webSocket(
                method = HttpMethod.Get,
                host = wssUrl,
                path = DEFAULT_WSS_PATH,
            ) {
                outgoing.send(
                    Frame.Text(jsonAdapter.toJson(subscribeRequest))
                )

                while(isActive) {
                    val othersMessage = incoming.receive() as? Frame.Text
                    othersMessage?.let {
                        emit(
                            (othersMessage.readText())
                        )
                    }
                }
            }

        } catch (e: ServerResponseException) {
            emit("Ktor Error getting remote items: ${e.response.status.description}")
        } catch (e: ClientRequestException) {
            emit("Ktor Error getting remote items: ${e.response.status.description}")
        } catch (e: RedirectResponseException) {
            emit("KtorError getting remote items: ${e.response.status.description}")
        }
        catch (e: Exception) {
            emit("KtorError getting remote items: ${e.localizedMessage}")
        }
    }

    private companion object {
        private const val DEFAULT_WSS_PATH = "/"
    }
}

