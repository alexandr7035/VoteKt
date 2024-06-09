package by.alexandr7035.votekt.ui.utils

import org.kethereum.model.Address

fun Address.prettify(): String {
    return this.hex.prettifyAddress()
}
