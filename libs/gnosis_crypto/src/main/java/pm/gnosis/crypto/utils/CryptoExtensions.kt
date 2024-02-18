package pm.gnosis.crypto.utils

import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.bouncycastle.jcajce.provider.digest.RIPEMD160
import java.math.BigInteger
import java.security.NoSuchAlgorithmException


fun ByteString.hash160(): ByteString {
    try {
        val digest = RIPEMD160.Digest().digest(sha256().toByteArray())
        return digest.toByteString(0, digest.size)
    } catch (e: NoSuchAlgorithmException) {
        throw AssertionError(e)
    }
}

fun BigInteger.toBytes(numBytes: Int): ByteArray {
    val bytes = ByteArray(numBytes)
    val biBytes = toByteArray()
    val start = if (biBytes.size == numBytes + 1) 1 else 0
    val length = Math.min(biBytes.size, numBytes)
    System.arraycopy(biBytes, start, bytes, numBytes - length, length)
    return bytes
}

fun ByteString.bigInt(): BigInteger {
    return BigInteger(1, toByteArray())
}
