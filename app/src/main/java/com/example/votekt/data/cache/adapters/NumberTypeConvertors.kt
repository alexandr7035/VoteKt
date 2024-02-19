package com.example.votekt.data.cache.adapters

import androidx.room.TypeConverter
import by.alexandr7035.utils.parseToBigInteger
import by.alexandr7035.utils.toHexString
import java.math.BigInteger

class NumberTypeConvertors {
    @TypeConverter
    fun fromBigInteger(number: BigInteger?): String? {
        return number?.toHexString()
    }

    @TypeConverter
    fun toWei(number: String?): BigInteger? {
        return number?.parseToBigInteger()
    }
}
