package com.example.votekt.data.web3_core.utils

import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

fun BigInteger.toEther(from: Convert.Unit = Convert.Unit.WEI): BigDecimal {
    return when (from) {
        Convert.Unit.WEI -> Convert.fromWei(this.toBigDecimal(), Convert.Unit.ETHER)
        Convert.Unit.KWEI -> TODO()
        Convert.Unit.MWEI -> TODO()
        Convert.Unit.GWEI -> TODO()
        Convert.Unit.SZABO -> TODO()
        Convert.Unit.FINNEY -> TODO()
        Convert.Unit.ETHER -> TODO()
        Convert.Unit.KETHER -> TODO()
        Convert.Unit.METHER -> TODO()
        Convert.Unit.GETHER -> TODO()
    }
}