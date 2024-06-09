package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.PinCode

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
