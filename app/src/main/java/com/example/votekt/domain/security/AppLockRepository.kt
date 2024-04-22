package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.AuthenticationResult
import com.example.votekt.domain.security.model.BiometricsAvailability
import com.example.votekt.domain.security.model.EncryptedPinCode
import com.example.votekt.domain.security.model.PinCode
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
}
