package by.alexandr7035.votekt.domain.security

class CheckAppLockedWithBiometricsUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Boolean {
        return appLockRepository.checkIfAppLockedWithBiometrics()
    }
}
