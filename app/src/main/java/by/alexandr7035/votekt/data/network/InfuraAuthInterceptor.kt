package by.alexandr7035.votekt.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class InfuraAuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val originalHttpUrl: HttpUrl = originalRequest.url

        val url: HttpUrl = originalHttpUrl.newBuilder()
            .addPathSegments(VERSION_PATH)
            .addPathSegments(apiKey)
            .build()

        println("$url")

        val newRequest: Request = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val VERSION_PATH = "v3/"
    }
}
