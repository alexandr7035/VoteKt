package by.alexandr7035.ethereum.model.extensions

import by.alexandr7035.utils.hexAsBigInteger
import org.kethereum.model.Transaction

// Type of a transaction with priority fee. Defined in EIP-1559
fun Transaction.getType() = "0x02".hexAsBigInteger().toByte()
