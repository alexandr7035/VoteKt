package by.alexandr7035.ethereumimpl.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EthWebsocketEventRaw(
    @Json(name = "jsonrpc") val jsonRpc: String,
    @Json(name = "params") val params: EthWebsocketEventParams?
)

@JsonClass(generateAdapter = true)
data class EthWebsocketEventParams(
    @Json(name = "subscription") val subscription: String,
    @Json(name = "result") val result: EthWebsocketEventResult
)

@JsonClass(generateAdapter = true)
data class EthWebsocketEventResult(
    @Json(name = "data") val data: String,
    @Json(name = "address") val address: String,
    @Json(name = "topics") val topics: List<String>,
)
