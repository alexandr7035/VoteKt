package by.alexandr7035.votekt.data.cache.adapters

import androidx.room.TypeConverter
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.utils.parseToBigInteger
import by.alexandr7035.utils.toHexString

class WeiTypeConvertor {
    @TypeConverter
    fun fromWei(wei: Wei?): String? {
        return wei?.value?.toHexString()
    }

    @TypeConverter
    fun toWei(wei: String?): Wei? {
        return wei?.parseToBigInteger()?.let { Wei(it) }
    }
}
