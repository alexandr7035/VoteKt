package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository

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
