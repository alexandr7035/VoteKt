package by.alexandr7035.votekt.data.repository

import android.content.Context
import android.util.Base64
import androidx.biometric.BiometricManager
import by.alexandr7035.votekt.data.cache.PrefKeys
import by.alexandr7035.votekt.data.security.BiometricsManager
import by.alexandr7035.votekt.data.security.CryptoUtils
import by.alexandr7035.votekt.data.security.model.BiometricEncryptedPinWrapper
import by.alexandr7035.votekt.domain.model.security.AuthenticationResult
import by.alexandr7035.votekt.domain.model.security.BiometricsAvailability
import by.alexandr7035.votekt.domain.model.security.EncryptedPinCode
import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository
import com.cioccarellia.ksprefs.KsPrefs
import com.squareup.moshi.Moshi
import javax.crypto.Cipher

class AppLockRepositoryImpl(
    private val ksPrefs: KsPrefs,
    private val context: Context,
    private val biometricsManager: BiometricsManager,
    private val moshi: Moshi,
) : AppLockRepository {

    override fun setupPinLock(pinCode: PinCode) {
        savePin(pin = pinCode.value)
    }

    override fun setupLockWithBiometrics(isLocked: Boolean, pinCode: PinCode) {
        val cipher = biometricsManager.getEncryptionCipher()
        val encryptedPin = biometricsManager.encryptData(pinCode.value, cipher)

        val jsonAdapter = moshi.adapter(BiometricEncryptedPinWrapper::class.java)

        val encryptedPinJson = jsonAdapter.toJson(encryptedPin)

        with(ksPrefs) {
            push(PrefKeys.BIOMETRICS_FLAG, isLocked)
            push(PrefKeys.BIOMETRICS_ENCRYPTED_PIN_KEY, encryptedPinJson.toString())
        }
    }

    override fun removeAppLock() {
        with(ksPrefs) {
            push(PrefKeys.PIN_KEY, "")
            push(PrefKeys.PIN_SALT_KEY, "")
            push(PrefKeys.BIOMETRICS_FLAG, false)
            push(PrefKeys.BIOMETRICS_ENCRYPTED_PIN_KEY, "")
            biometricsManager.deleteKey()
        }
    }

    override fun getBiometricsDecryptionCipher(): Cipher? {
        return biometricsManager.getDecryptionCipher()
    }

    override fun getEncryptedPinWithBiometrics(): EncryptedPinCode? {
        val jsonAdapter = moshi.adapter(BiometricEncryptedPinWrapper::class.java)
        val json = ksPrefs.pull(PrefKeys.BIOMETRICS_ENCRYPTED_PIN_KEY, "")

        return if (json.isNotBlank()) {
            jsonAdapter.fromJson(json)?.let { pinWrapper ->
                EncryptedPinCode(pinWrapper.ciphertext)
            }
        } else {
            null
        }
    }

    override fun decryptPinWithBiometrics(
        encryptedPinCode: EncryptedPinCode,
        cipher: Cipher
    ): PinCode {
        val decrypted = biometricsManager.decryptData(
            encryptedPinCode.value,
            cipher
        )

        return PinCode(decrypted)
    }

    override fun authenticateWithPin(pinCode: PinCode): AuthenticationResult {
        return if (isPinValid(pinCode.value)) {
            AuthenticationResult.Success
        } else {
            AuthenticationResult.Failure
        }
    }

    private fun savePin(pin: String) {
        val salt = CryptoUtils.generateSalt()

        val secretKey = CryptoUtils.generatePbkdf2Key(
            passphraseOrPin = pin.toCharArray(),
            salt = salt
        )

        val encodedPinData = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
        val encodedSalt = Base64.encodeToString(salt, Base64.DEFAULT)

        with(ksPrefs) {
            push(PrefKeys.PIN_KEY, encodedPinData)
            push(PrefKeys.PIN_SALT_KEY, encodedSalt)
        }
    }

    private fun isPinValid(pin: String): Boolean {
        val storedSalt = ksPrefs.pull<String>(PrefKeys.PIN_SALT_KEY)
        val decodedSalt = Base64.decode(storedSalt, Base64.DEFAULT)

        val storedPinData = ksPrefs.pull<String>(PrefKeys.PIN_KEY)
        val decodedPinData = Base64.decode(storedPinData, Base64.DEFAULT)

        val enteredPinData = CryptoUtils.generatePbkdf2Key(pin.toCharArray(), decodedSalt)

        return decodedPinData contentEquals enteredPinData.encoded
    }

    override fun checkIfAppLocked(): Boolean {
        return ksPrefs.pull(PrefKeys.PIN_SALT_KEY, "").isNotBlank() &&
            ksPrefs.pull(PrefKeys.PIN_KEY, "").isNotBlank()
    }

    override fun checkIfAppLockedWithBiometrics(): Boolean {
        return ksPrefs.pull(PrefKeys.BIOMETRICS_FLAG, false)
    }

    override fun checkBiometricsAvailable(): BiometricsAvailability {
        val biometricManager = BiometricManager.from(context)
        val result = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
                or BiometricManager.Authenticators.BIOMETRIC_WEAK
        )

        return when (result) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                BiometricsAvailability.Available
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                BiometricsAvailability.NotEnabled
            }
            // All cases where not available
            else -> {
                BiometricsAvailability.NotAvailable
            }
        }
    }
}
