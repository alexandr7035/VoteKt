package by.alexandr7035.ethereumimpl.model

import by.alexandr7035.ethereum.model.EthNodeMethods
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_CALL
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_ESTIMATE_GAS
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_GAS_PRICE
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_GET_TRANSACTION_COUNT
import by.alexandr7035.ethereum.model.EthNodeMethods.FUNCTION_SEND_RAW_TRANSACTION
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.model.asString
import by.alexandr7035.ethereum.model.extensions.getType
import by.alexandr7035.ethereum.model.requests.EthBalance
import by.alexandr7035.ethereum.model.requests.EthCall
import by.alexandr7035.ethereum.model.requests.EthEstimateGas
import by.alexandr7035.ethereum.model.requests.EthGasPrice
import by.alexandr7035.ethereum.model.requests.EthGetTransactionCount
import by.alexandr7035.ethereum.model.requests.EthRequest
import by.alexandr7035.ethereum.model.requests.EthSendRawTransaction
import by.alexandr7035.utils.hexAsBigIntegerOrNull
import by.alexandr7035.utils.toHexString
import org.kethereum.model.Transaction
import org.komputing.khex.extensions.toHexString
import java.math.BigInteger

sealed class RpcRequest<out T : EthRequest<*>>(val raw: T) {
    abstract fun request(): JsonRpcRequest
    abstract fun parse(response: JsonRpcResult)
}

class RpcBalanceRequest(raw: EthBalance) : RpcRequest<EthBalance>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = EthNodeMethods.FUNCTION_GET_BALANCE,
            params = listOf(raw.address.hex, raw.block.asString()),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<Wei>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(Wei(it)) }
            ?: EthRequest.Response.Failure("Invalid balance!")
    }
}

class RpcSendRawTransaction(raw: EthSendRawTransaction) : RpcRequest<EthSendRawTransaction>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_SEND_RAW_TRANSACTION,
            params = listOf(raw.signedData),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response =
            response.error?.let { EthRequest.Response.Failure<String>(it.message) }
                ?: response.result?.let { EthRequest.Response.Success(response.result) }
                ?: EthRequest.Response.Failure("Missing result")
    }
}

class RpcTransactionCountRequest(raw: EthGetTransactionCount) :
    RpcRequest<EthGetTransactionCount>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_GET_TRANSACTION_COUNT,
            params = arrayListOf(raw.from.hex, raw.block.asString()),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<BigInteger>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(it) }
            ?: EthRequest.Response.Failure("Invalid transaction count!")
    }
}

class RpcEstimateGasRequest(raw: EthEstimateGas) : RpcRequest<EthEstimateGas>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_ESTIMATE_GAS,
            params = listOf(
                raw.transaction.toCallParams(raw.from.hex)
            ),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<BigInteger>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(it) }
            ?: EthRequest.Response.Failure("Invalid estimate!")
    }
}

class RpcGasPriceRequest(raw: EthGasPrice) : RpcRequest<EthGasPrice>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_GAS_PRICE,
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<BigInteger>(it.message) }
            ?: response.result?.hexAsBigIntegerOrNull()?.let { EthRequest.Response.Success(it) }
            ?: EthRequest.Response.Failure("Invalid gas price!")
    }
}

class RpcCallRequest(raw: EthCall) : RpcRequest<EthCall>(raw) {
    override fun request() =
        JsonRpcRequest(
            method = FUNCTION_CALL,
            params = listOf(
                ContractCallParams(
                    to = raw.to.hex,
                    data = raw.data
                )
            ),
            id = raw.id
        )

    override fun parse(response: JsonRpcResult) {
        raw.response = response.error?.let { EthRequest.Response.Failure<String>(it.message) }
            ?: response.result?.let { EthRequest.Response.Success(response.result) }
            ?: EthRequest.Response.Failure("Missing result")
    }
}

fun Transaction.toCallParams(from: String) =
    TransactionCallParams(
        type = this.getType().toHexString(),
        chainId = this.chain?.toHexString(),
        from = from,
        to = this.to?.hex,
        value = this.value?.toHexString() ?: "0x0",
        data = this.input.toHexString(),
        nonce = this.nonce?.toHexString(),
        gas = this.gasPrice?.toHexString(),
        maxPriorityFeePerGas = this.maxPriorityFeePerGas?.toHexString(),
        maxFeePerGas = this.maxFeePerGas?.toHexString()
    )
