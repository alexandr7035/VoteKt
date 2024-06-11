package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.EncryptedPinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository

class GetBiometricEncryptedPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): EncryptedPinCode? {
        return appLockRepository.getEncryptedPinWithBiometrics()
    }
}
