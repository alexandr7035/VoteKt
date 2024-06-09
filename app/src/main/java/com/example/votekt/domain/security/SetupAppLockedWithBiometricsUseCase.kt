package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.PinCode

class SetupAppLockedWithBiometricsUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(
        isLocked: Boolean = true,
        pinToEncrypt: PinCode,
    ) {
        return appLockRepository.setupLockWithBiometrics(
            isLocked = isLocked,
            pinCode = pinToEncrypt
        )
    }
}
