package by.alexandr7035.votekt.ui.utils

import org.kethereum.model.Address

fun Address.prettify(
    prefixLength: Int = 5,
    suffixLength: Int = 4,
): String {
    return this.hex.prettifyAddress(prefixLength, suffixLength)
}
