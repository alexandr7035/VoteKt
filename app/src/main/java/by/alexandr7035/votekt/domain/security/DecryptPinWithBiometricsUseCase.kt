package by.alexandr7035.votekt.domain.security

import by.alexandr7035.votekt.domain.security.model.EncryptedPinCode
import by.alexandr7035.votekt.domain.security.model.PinCode
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
