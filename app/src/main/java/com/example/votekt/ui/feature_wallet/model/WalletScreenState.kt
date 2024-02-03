package com.example.votekt.ui.feature_wallet.model

import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class WalletScreenState(
    val isBalanceLoading: Boolean = true,
    val balance: String = "",
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed()
)
