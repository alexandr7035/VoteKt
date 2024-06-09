package by.alexandr7035.votekt.domain.security

class CheckAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Boolean {
        return appLockRepository.checkIfAppLocked()
    }
}
