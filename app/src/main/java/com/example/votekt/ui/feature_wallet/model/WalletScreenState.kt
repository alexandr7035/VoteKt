package com.example.votekt.ui.feature_wallet.model

import com.example.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class WalletScreenState(
    val isBalanceLoading: Boolean = true,
    val balanceFormatted: String? = null,
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed(),
    val error: UiErrorMessage? = null
)
