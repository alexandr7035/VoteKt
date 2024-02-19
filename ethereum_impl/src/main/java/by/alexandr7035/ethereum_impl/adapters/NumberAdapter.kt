package by.alexandr7035.ethereum_impl.adapters

import by.alexandr7035.utils.hexAsBigInteger
import by.alexandr7035.utils.parseToBigInteger
import by.alexandr7035.utils.toHexString
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.math.BigDecimal
import java.math.BigInteger


class HexNumberAdapter {
    @ToJson
    fun toJson(@HexNumber hexNumber: BigInteger): String = hexNumber.toHexString()

    @FromJson
    @HexNumber
    fun fromJson(hexNumber: String): BigInteger = hexNumber.hexAsBigInteger()
}

class DecimalNumberAdapter {
    @ToJson
    fun toJson(@DecimalNumber bigInteger: BigInteger): String = bigInteger.toString()

    @FromJson
    @DecimalNumber
    fun fromJson(decimalNumber: String): BigInteger = decimalNumber.toBigInteger()
}

class BigDecimalNumberAdapter {
    @ToJson
    fun toJson(@BigDecimalNumber bigDecimal: BigDecimal): String = bigDecimal.toString()

    @FromJson
    @BigDecimalNumber
    fun fromJson(decimalNumber: String): BigDecimal = decimalNumber.toBigDecimal()
}

class DefaultNumberAdapter {
    @ToJson
    fun toJson(hexNumber: BigInteger): String = hexNumber.toHexString()

    @FromJson
    fun fromJson(hexNumber: String): BigInteger = hexNumber.parseToBigInteger()
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HexNumber

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class DecimalNumber

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class BigDecimalNumber
