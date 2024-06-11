package by.alexandr7035.votekt.domain.repository

import by.alexandr7035.votekt.domain.model.security.AuthenticationResult
import by.alexandr7035.votekt.domain.model.security.BiometricsAvailability
import by.alexandr7035.votekt.domain.model.security.EncryptedPinCode
import by.alexandr7035.votekt.domain.model.security.PinCode
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
