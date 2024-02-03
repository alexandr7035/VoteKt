package com.example.votekt.data.account

import java.math.BigDecimal

data class AccountBalance(
    val amount: BigDecimal,
    // Others not implemented yet
    val asset: AssetType = AssetType.ETH
)
