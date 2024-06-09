package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.AuthenticationResult
import by.alexandr7035.votekt.domain.security.model.BiometricsAvailability
import by.alexandr7035.votekt.domain.security.model.EncryptedPinCode
import by.alexandr7035.votekt.domain.security.model.PinCode
import javax.crypto.Cipher

interface AppLockRepository {
    fun checkIfAppLocked(): Boolean
    fun setupPinLock(pinCode: PinCode)
    fun setupLockWithBiometrics(
        isLocked: Boolean,
        pinCode: PinCode,
    )
    fun authenticateWithPin(pinCode: PinCode): AuthenticationResult

    fun checkBiometricsAvailable(): BiometricsAvailability
    fun checkIfAppLockedWithBiometrics(): Boolean

    fun getBiometricsDecryptionCipher(): Cipher?
    fun getEncryptedPinWithBiometrics(): EncryptedPinCode?
    fun decryptPinWithBiometrics(
        encryptedPinCode: EncryptedPinCode,
        cipher: Cipher
    ): PinCode

    fun removeAppLock()
}
