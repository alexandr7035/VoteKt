package com.example.votekt.core.crypto

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object BalanceFormatter {

    private val smallValueFormatter: DecimalFormat
    private val formatter1k: DecimalFormat
    private val formatter10k: DecimalFormat
    private val formatter100k: DecimalFormat
    private val formatter1M: DecimalFormat
    private val formatter10M: DecimalFormat
    private val formatter100M: DecimalFormat
    private val formatterBigNumber: DecimalFormat

    private val decimalSeparator: Char
    private val groupingSeparator: Char

    init {
        val symbols = DecimalFormatSymbols(Locale.US)
        decimalSeparator = symbols.decimalSeparator
        groupingSeparator = symbols.groupingSeparator

        smallValueFormatter = DecimalFormat("#.#####", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }

        formatter1k = DecimalFormat("#.######", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatter10k = DecimalFormat("#,###.##", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatter100k = DecimalFormat("##,###.##", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatter1M = DecimalFormat("###,###.##", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatter10M = DecimalFormat("#,###,###.#", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatter100M = DecimalFormat("#,###,##", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
        formatterBigNumber = DecimalFormat("#.##", symbols).apply {
            roundingMode = RoundingMode.DOWN
            isGroupingUsed = false
        }
    }

    fun formatAmount(amount: BigDecimal): String = when {
        amount <= BigDecimal.ZERO -> {
            "0"
        }
        amount < LOWEST_LIMIT -> {
            "< 0${decimalSeparator}00001"
        }
        amount < SMALL_LIMIT -> {
            smallValueFormatter.format(amount)
        }
        amount < THOUSAND_LIMIT -> {
            formatter1k.format(amount)
        }
        amount < TEN_THOUSAND_LIMIT -> {
            formatter10k.format(amount)
        }
        amount < HUNDRED_THOUSAND_LIMIT -> {
            formatter100k.format(amount)
        }
        amount < MILLION_LIMIT -> {
            formatter1M.format(amount)
        }
        amount < TEN_MILLION_LIMIT -> {
            formatter10M.format(amount)
        }
        amount < HUNDRED_MILLION_LIMIT -> {
            formatter100M.format(amount)
        }
        amount < BILLION_LIMIT -> {
            val formattedValue = amount.divide(BigDecimal.TEN.pow(6))
            formatterBigNumber.format(formattedValue) + "M"
        }
        amount < TRILLION_LIMIT -> {
            val formattedValue = amount.divide(BigDecimal.TEN.pow(9))
            formatterBigNumber.format(formattedValue) + "B"
        }
        amount < THOUSAND_TRILLION_LIMIT -> {
            val formattedValue = amount.divide(BigDecimal.TEN.pow(12))
            formatterBigNumber.format(formattedValue) + "T"
        }
        else -> {
            "> 999T"
        }
    }

    fun formatAmountWithSymbol(
        amount: BigDecimal,
        symbol: String
    ): String {
        return "${formatAmount(amount)} ${symbol}"
    }

    private val LOWEST_LIMIT = BigDecimal.ONE.divide(BigDecimal.TEN.pow(5))
    private val SMALL_LIMIT = BigDecimal.ONE
    private val THOUSAND_LIMIT = BigDecimal.TEN.pow(3)
    private val TEN_THOUSAND_LIMIT = BigDecimal.TEN.pow(4)
    private val HUNDRED_THOUSAND_LIMIT = BigDecimal.TEN.pow(5)
    private val MILLION_LIMIT = BigDecimal.TEN.pow(6)
    private val TEN_MILLION_LIMIT = BigDecimal.TEN.pow(7)
    private val HUNDRED_MILLION_LIMIT = BigDecimal.TEN.pow(8)
    private val BILLION_LIMIT = BigDecimal.TEN.pow(9)
    private val TRILLION_LIMIT = BigDecimal.TEN.pow(12)
    private val THOUSAND_TRILLION_LIMIT = BigDecimal.TEN.pow(15)
}
