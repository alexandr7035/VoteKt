package by.alexandr7035.ethereum_impl.adapters

import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.ethereum.utils.parseToBigInteger
import by.alexandr7035.ethereum.utils.toHexString
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class WeiAdapter {
    @ToJson
    fun toJson(wei: Wei): String =
        wei.value.toHexString()

    @FromJson
    fun fromJson(wei: String): Wei {
        return Wei(wei.parseToBigInteger())
    }
}