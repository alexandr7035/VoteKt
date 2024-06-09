package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.EncryptedPinCode

class GetBiometricEncryptedPinUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): EncryptedPinCode? {
        return appLockRepository.getEncryptedPinWithBiometrics()
    }
}
