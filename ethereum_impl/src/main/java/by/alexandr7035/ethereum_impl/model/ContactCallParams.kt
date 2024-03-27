package by.alexandr7035.ethereum_impl.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContractCallParams(
    @Json(name = "to") val to: String,
    @Json(name = "data") val data: String
) {
    fun callRequest(id: Int, block: String = "latest"): JsonRpcRequest {
        return JsonRpcRequest(
            id = id,
            method = "eth_call",
            params = listOf(this, block)
        )
    }
}