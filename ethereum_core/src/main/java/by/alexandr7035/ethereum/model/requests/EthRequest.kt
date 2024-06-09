package by.alexandr7035.ethereum.model.requests

import by.alexandr7035.ethereum.errors.RequestFailedException
import by.alexandr7035.ethereum.errors.RequestNotExecutedException

sealed class EthRequest<T>(open val id: Int) {
    var response: Response<T>? = null

    fun result(): T? = (response as? Response.Success)?.data

    @Throws(RequestFailedException::class, RequestNotExecutedException::class)
    fun checkedResult(errorMsg: String): T =
        response.let {
            when (it) {
                is Response.Success ->
                    it.data
                is Response.Failure -> {
                    val msg = it.error + (errorMsg.let { " ($it)" } ?: "")
                    println("$TAG eth req fail: $msg")
                    throw RequestFailedException(msg)
                }
                null ->
                    throw RequestNotExecutedException(errorMsg)
            }
        }

    sealed class Response<T> {
        data class Success<T>(val data: T) : Response<T>()
        data class Failure<T>(val error: String) : Response<T>()
    }

    private companion object {
        const val TAG = "ETH_CALL"
    }
}
