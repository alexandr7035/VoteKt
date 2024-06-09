package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.EncryptedPinCode

class GetBiometricEncryptedPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): EncryptedPinCode? {
        return appLockRepository.getEncryptedPinWithBiometrics()
    }
}
