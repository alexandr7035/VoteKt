package by.alexandr7035.votekt.domain.usecase.applock

import by.alexandr7035.votekt.domain.repository.AppLockRepository

class CheckAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Boolean {
        return appLockRepository.checkIfAppLocked()
    }
}
