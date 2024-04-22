package com.example.votekt.domain.security

import javax.crypto.Cipher

class GetBiometricDecryptionCipherUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Cipher? {
        return appLockRepository.getBiometricsDecryptionCipher()
    }
}