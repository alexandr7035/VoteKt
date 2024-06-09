package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.AuthenticationResult
import by.alexandr7035.votekt.domain.security.model.PinCode

class AuthenticateWithPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pin: PinCode): AuthenticationResult {
        return appLockRepository.authenticateWithPin(pin)
    }
}
