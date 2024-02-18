package pm.gnosis.crypto

import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException


object KeyGenerator {

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun masterNode(seed: ByteArray): HDNode {
        val byteStringSeed = ByteString.of(*seed)
        val hash = byteStringSeed.hmacSha512(MASTER_SECRET.encodeUtf8())
        return HDNode(KeyPair.fromPrivate(hash.substring(0, 32).toByteArray()), hash.substring(32), 0, 0, ByteString.of(0, 0, 0, 0))
    }

    private const val MASTER_SECRET = "Bitcoin seed"

}
