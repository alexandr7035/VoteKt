package com.example.votekt.data.helpers

import com.example.votekt.domain.core.AppError
import com.example.votekt.domain.core.ErrorType
import com.example.votekt.domain.core.OperationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T : Any> executeWeb3Call(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    remoteCall: () -> T,
): OperationResult<T> {
    return withContext(coroutineContext) {
        try {
            val response = remoteCall()
            OperationResult.Success(response)
        } catch (ex: Throwable) {
            OperationResult.Failure(AppError(ErrorType.fromThrowable(ex)))
        }
    }
}