package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.model.security.EncryptedPinCode
import by.alexandr7035.votekt.domain.model.security.PinCode
import by.alexandr7035.votekt.domain.repository.AppLockRepository
import javax.crypto.Cipher

class DecryptPinWithBiometricsUseCase(
    private val appLockRepository: AppLockRepository,
) {
    fun invoke(
        encryptedPinCode: EncryptedPinCode,
        cipher: Cipher
    ): PinCode {
        return appLockRepository.decryptPinWithBiometrics(encryptedPinCode, cipher)
    }
}
