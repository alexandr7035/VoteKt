package by.alexandr7035.utils

import java.math.BigInteger

fun BigInteger.asEthereumAddressString() = this.toString(16).padStart(40, '0').addHexPrefix()

fun ByteArray.asEthereumAddressString() = this.asBigInteger().toString(16).padStart(40, '0').addHexPrefix()