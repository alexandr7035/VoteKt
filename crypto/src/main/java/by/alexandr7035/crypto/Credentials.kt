package by.alexandr7035.crypto

import org.kethereum.model.Address
import org.kethereum.model.ECKeyPair

data class Credentials(
    val address: Address,
    val keyPair: ECKeyPair
)
