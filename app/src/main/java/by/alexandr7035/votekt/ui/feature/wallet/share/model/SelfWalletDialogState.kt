package by.alexandr7035.votekt.ui.feature.wallet.share.model

import by.alexandr7035.votekt.ui.core.resources.UiText
import org.kethereum.model.Address

data class SelfWalletDialogState(
    val address: Address? = null,
    val error: UiText? = null,
    val isLoading: Boolean = true,
)
