package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.EncryptedPinCode
import com.example.votekt.domain.security.model.PinCode
import javax.crypto.Cipher

class DecryptPinWithBiometricsUseCase(
    private val appLockRepository: AppLockRepository,
) {
    fun invoke(
        encryptedPinCode: EncryptedPinCode,
        cipher: Cipher
    ): PinCode {
        return appLockRepository.decryptPinWithBiometrics(encryptedPinCode, cipher)
    }
}