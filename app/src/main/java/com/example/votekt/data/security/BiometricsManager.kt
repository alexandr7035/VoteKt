package com.example.votekt.data.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.votekt.data.security.model.BiometricEncryptedPinWrapper
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

interface BiometricsManager {
    fun getDecryptionCipher(): Cipher
    fun getEncryptionCipher(): Cipher

    fun encryptData(plaintext: String, cipher: Cipher): BiometricEncryptedPinWrapper
    fun decryptData(ciphertext: ByteArray, cipher: Cipher): String

    fun deleteKey()
}


class BiometricsManagerImpl : BiometricsManager {

    override fun encryptData(plaintext: String, cipher: Cipher): BiometricEncryptedPinWrapper {
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charset.forName("UTF-8")))
        return BiometricEncryptedPinWrapper(ciphertext)
    }

    override fun decryptData(ciphertext: ByteArray, cipher: Cipher): String {
        val plaintext = cipher.doFinal(ciphertext)
        return String(plaintext, Charset.forName("UTF-8"))
    }

    override fun getDecryptionCipher(): Cipher {
        val cipher = getCipher()
        val privateKey = getOrCreateKey(KeyType.PRIVATE_KEY) as PrivateKey
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher
    }

    override fun getEncryptionCipher(): Cipher {
        val cipher = getCipher()
        try {
            val publicKey = getOrCreateKey(KeyType.PUBLIC_KEY) as PublicKey
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        } catch (e: java.lang.ClassCastException) {
            deleteKey()
        }
        return cipher
    }

    private fun getOrCreateKey(
        keyType: KeyType = KeyType.PRIVATE_KEY,
    ): Key {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)

        when (keyType) {
            KeyType.PUBLIC_KEY -> {
                keyStore.getCertificate(KEY_NAME)?.let { return it.publicKey as PublicKey }
            }
            KeyType.PRIVATE_KEY -> {
                keyStore.getKey(KEY_NAME, null)?.let { return it as PrivateKey }
            }
        }

        return generateNewKeyAndReturn(keyType)
    }

    private fun generateNewKeyAndReturn(returnedKeyType: KeyType): Key {
        // if you reach here, then a new PrivateKey must be generated for that keyName
        val builder = KeyGenParameterSpec.Builder(
            KEY_NAME,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        builder.apply {
            setBlockModes(ASYMMETRIC_ENCRYPTION_BLOCK_MODE)
            setEncryptionPaddings(ASYMMETRIC_ENCRYPTION_PADDING)
            setKeySize(RSA_KEY_SIZE)
            setUserAuthenticationRequired(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setUserAuthenticationParameters(
                    AUTH_INVALIDATION_TIMEOUT,
                    KeyProperties.AUTH_DEVICE_CREDENTIAL
                            or KeyProperties.AUTH_BIOMETRIC_STRONG
                )
            } else {
                builder.setUserAuthenticationValidityDurationSeconds(AUTH_INVALIDATION_TIMEOUT)
            }
        }

        val keyGenParams = builder.build()
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            KEYSTORE_PROVIDER
        )
        keyPairGenerator.initialize(keyGenParams)
        val keyPair = keyPairGenerator.genKeyPair()

        return when (returnedKeyType) {
            KeyType.PUBLIC_KEY -> keyPair.public
            KeyType.PRIVATE_KEY -> keyPair.private
        }
    }

    private fun getCipher(): Cipher {
        val transformation = "$ASYMMETRIC_ENCRYPTION_ALGORITHM/$ASYMMETRIC_ENCRYPTION_BLOCK_MODE/$ASYMMETRIC_ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    override fun deleteKey() {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER)
        keyStore.load(null)
        keyStore.deleteEntry(KEY_NAME)
    }

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_NAME = "VOTE_KT_KEY"

        private const val RSA_KEY_SIZE = 1024
        private const val ASYMMETRIC_ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA
        private const val ASYMMETRIC_ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
        private const val ASYMMETRIC_ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1

        private const val AUTH_INVALIDATION_TIMEOUT = 0
    }

    private enum class KeyType {
        PUBLIC_KEY,
        PRIVATE_KEY,
    }
}

