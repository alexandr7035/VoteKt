package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.repository.AppLockRepository
import javax.crypto.Cipher

class GetBiometricDecryptionCipherUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Cipher? {
        return appLockRepository.getBiometricsDecryptionCipher()
    }
}
