package com.example.votekt.domain.security

import com.example.votekt.domain.security.model.AuthenticationResult
import com.example.votekt.domain.security.model.PinCode

class AuthenticateWithPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pin: PinCode): AuthenticationResult {
        return appLockRepository.authenticateWithPin(pin)
    }
}
