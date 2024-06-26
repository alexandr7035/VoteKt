package by.alexandr7035.votekt.ui.feature.wallet.model

import by.alexandr7035.votekt.domain.model.contract.ContractState
import by.alexandr7035.votekt.ui.UiErrorMessage
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed
import org.kethereum.model.Address

data class WalletScreenState(
    val isHeaderVisible: Boolean = true,
    val address: Address = Address(""),
    val balanceState: SelfBalanceState = SelfBalanceState(),
    val contractState: ContractState? = null,
    val navigationEvent: StateEventWithContent<WalletScreenNavigationEvent> = consumed(),
    val error: UiErrorMessage? = null,
    val isSelfWalletDialogShown: Boolean = false,
)

data class SelfBalanceState(
    val isBalanceLoading: Boolean = true,
    val isBalanceError: Boolean = false,
    val balanceFormatted: String? = null,
)
