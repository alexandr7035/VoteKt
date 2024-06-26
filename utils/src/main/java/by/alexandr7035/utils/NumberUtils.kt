@file:Suppress("MagicNumber")

package by.alexandr7035.utils

import java.math.BigInteger

fun BigInteger.toHexString() = this.toString(16).addHexPrefix()

fun String.parseToBigInteger(): BigInteger =
    if (startsWith("0x")) hexAsBigInteger() else decimalAsBigInteger()

fun String.hexAsBigInteger() = BigInteger(this.removePrefix("0x"), 16)
fun String.decimalAsBigInteger() = BigInteger(this, 10)

private const val HEX_PREFIX = "0x"
fun String.addHexPrefix() = if (!this.startsWith(HEX_PREFIX)) "$HEX_PREFIX$this" else this

fun String.removeHexPrefix() = if (this.startsWith(HEX_PREFIX)) this.removePrefix(HEX_PREFIX) else this

fun ByteArray.asBigInteger() = BigInteger(1, this)

fun String.hexAsBigIntegerOrNull() = this.hexAsBigInteger()

fun Byte.toHexString() = this.toString(16).addHexPrefix()
