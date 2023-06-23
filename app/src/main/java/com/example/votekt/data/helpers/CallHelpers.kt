package com.example.votekt.data.helpers

import com.example.votekt.data.AppError
import com.example.votekt.data.OperationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import kotlin.coroutines.CoroutineContext

suspend fun <T : Any> executeWeb3Call(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    remoteCall: () -> T,
): OperationResult<T> {
    return withContext(coroutineContext) {
        try {
            val response = remoteCall()
            OperationResult.Success(response)
        } catch (ex: Exception) {
            val error = when (ex) {
                is ConnectException -> AppError.ConnectionError
                else -> AppError.UnknownError(ex.stackTrace.toString())
            }

            OperationResult.Failure(error)
        }
    }
}