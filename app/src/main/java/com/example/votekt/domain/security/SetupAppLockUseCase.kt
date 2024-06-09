package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.PinCode

class SetupAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pinCode: PinCode) {
        return appLockRepository.setupPinLock(pinCode)
    }
}
