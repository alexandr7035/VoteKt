package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.AuthenticationResult
import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository

class AuthenticateWithPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pin: PinCode): AuthenticationResult {
        return appLockRepository.authenticateWithPin(pin)
    }
}
