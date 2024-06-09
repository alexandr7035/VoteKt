package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.PinCode

class SetupAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pinCode: PinCode) {
        return appLockRepository.setupPinLock(pinCode)
    }
}
