package by.alexandr7035.ethereumimpl.api

import by.alexandr7035.ethereumimpl.model.JsonRpcBlockResult
import by.alexandr7035.ethereumimpl.model.JsonRpcRequest
import by.alexandr7035.ethereumimpl.model.JsonRpcResult
import by.alexandr7035.ethereumimpl.model.JsonRpcTransactionReceiptResult
import by.alexandr7035.ethereumimpl.model.JsonRpcTransactionResult
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitEthereumRpcApi {
    @POST("./")
    suspend fun receipt(
        @Body jsonRpcRequest: JsonRpcRequest
    ): JsonRpcTransactionReceiptResult

    @POST("./")
    suspend fun block(
        @Body jsonRpcRequest: JsonRpcRequest
    ): JsonRpcBlockResult

    @POST("./")
    suspend fun transaction(
        @Body jsonRpcRequest: JsonRpcRequest
    ): JsonRpcTransactionResult

    @POST("./")
    suspend fun post(
        @Body jsonRpcRequest: JsonRpcRequest
    ): JsonRpcResult

    @POST("./")
    suspend fun post(
        @Body jsonRpcRequest: Collection<JsonRpcRequest>
    ): Collection<JsonRpcResult>
}
