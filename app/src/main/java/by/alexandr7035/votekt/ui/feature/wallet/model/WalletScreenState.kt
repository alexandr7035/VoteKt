package by.alexandr7035.votekt.ui.feature.wallet.model

import by.alexandr7035.votekt.domain.model.contract.ContractState
import by.alexandr7035.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import org.kethereum.model.Address

data class WalletScreenState(
    val isBalanceLoading: Boolean = true,
    val isHeaderVisible: Boolean = true,
    val address: Address = Address(""),
    val contractState: ContractState? = null,
    val balanceFormatted: String? = null,
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed(),
    val error: UiErrorMessage? = null,
)
