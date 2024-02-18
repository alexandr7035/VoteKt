package com.example.votekt.ui.feature_wallet.model

import by.alexandr7035.ethereum.model.Address
import com.example.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class WalletScreenState(
    val isBalanceLoading: Boolean = true,
    val address: Address = Address(""),
    val balanceFormatted: String? = null,
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed(),
    val error: UiErrorMessage? = null
)
