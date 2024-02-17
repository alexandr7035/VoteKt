package by.alexandr7035.ethereum_impl.api

import by.alexandr7035.ethereum_impl.model.JsonRpcBlockResult
import by.alexandr7035.ethereum_impl.model.JsonRpcRequest
import by.alexandr7035.ethereum_impl.model.JsonRpcResult
import by.alexandr7035.ethereum_impl.model.JsonRpcTransactionReceiptResult
import by.alexandr7035.ethereum_impl.model.JsonRpcTransactionResult
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