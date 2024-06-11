package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository

class SetupAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(pinCode: PinCode) {
        return appLockRepository.setupPinLock(pinCode)
    }
}
