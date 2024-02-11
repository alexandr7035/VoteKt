package com.example.votekt.ui.feature_wallet.model

import com.example.votekt.ui.UiErrorMessage
import com.example.votekt.ui.common.BalanceUi
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class WalletScreenState(
    val isBalanceLoading: Boolean = true,
    val balance: BalanceUi? = null,
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed(),
    val error: UiErrorMessage? = null
)
