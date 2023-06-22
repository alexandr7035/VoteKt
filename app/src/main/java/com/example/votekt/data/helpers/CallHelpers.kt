package com.example.votekt.data.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T : Any> executeWeb3Call(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    remoteCall: () -> T,
): Result<T> {
    return withContext(coroutineContext) {
        try {
            val response = remoteCall()
            Result.success(response)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}