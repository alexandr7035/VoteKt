package com.example.votekt.ui.common

import com.example.votekt.data.account.AccountBalance
import com.example.votekt.data.account.AssetType
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@JvmInline
value class BalanceUi(val value: String) {
    companion object {
        fun fromBalanceWithAssetType(balance: AccountBalance): BalanceUi {
            val symbols = DecimalFormatSymbols(Locale.US)
            val decimalFormat = DecimalFormat("#,##0.##", symbols)
            decimalFormat.isGroupingUsed = false
            val formattedValue = decimalFormat.format(balance.amount)

            // Add currency prefixes
            return when (balance.asset) {
                AssetType.ETH -> {
                    BalanceUi("$formattedValue ETH")
                }
            }
        }
    }
}