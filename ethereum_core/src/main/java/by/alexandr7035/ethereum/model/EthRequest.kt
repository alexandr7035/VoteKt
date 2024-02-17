package by.alexandr7035.ethereum.model

import by.alexandr7035.ethereum.errors.RequestFailedException
import by.alexandr7035.ethereum.errors.RequestNotExecutedException

sealed class EthRequest<T>(open val id: Int) {
    var response: Response<T>? = null

    fun result(): T? = (response as? Response.Success)?.data

    @Throws(RequestFailedException::class, RequestNotExecutedException::class)
    fun checkedResult(errorMsg: String? = null): T =
        response.let {
            when (it) {
                is Response.Success ->
                    it.data
                is Response.Failure -> {
                    val msg = it.error + (errorMsg?.let { " ($it)" } ?: "")
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
}