package by.alexandr7035.ethereum.model

import java.math.BigDecimal
import java.math.BigInteger

data class Wei(val value: BigInteger) {
    constructor(value: String): this(BigInteger(value))

    constructor(value: Long): this(BigInteger.valueOf(value))

    fun toEther(scale: Int = 18): BigDecimal = BigDecimal(value).setScale(scale).div(WEI_TO_ETHER_MULTIPLIER)

    fun toGWei(scale: Int = 18): BigDecimal = BigDecimal(value).setScale(scale).divide(BigDecimal(10).pow(9)).stripTrailingZeros()

    operator fun compareTo(other: Wei) = this.value.compareTo(other.value)

    operator fun plus(other: Wei) = Wei(this.value + other.value)

    companion object {
        private val WEI_TO_ETHER_MULTIPLIER = BigDecimal(10).pow(18)
        private val WEI_TO_GWEI_MULTIPLIER = BigDecimal(10).pow(9)

        fun fromGWei(value: Double) = Wei((value.toBigDecimal() * WEI_TO_GWEI_MULTIPLIER).toBigInteger())
        fun fromEther(value: Double) = Wei((value.toBigDecimal() * WEI_TO_ETHER_MULTIPLIER).toBigInteger())
    }
}

val Int.WEI: Wei
    get() = Wei(this.toBigInteger())

val Double.GWEI: Wei
    get() = Wei.fromGWei(this)

val Int.GWEI: Wei
    get() = Wei.fromGWei(this.toDouble())

val Double.ETHER: Wei
    get() = Wei.fromEther(this)

val Int.ETHER: Wei
    get() = Wei.fromEther(this.toDouble())