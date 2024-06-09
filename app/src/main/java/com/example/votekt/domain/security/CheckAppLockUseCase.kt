package com.example.votekt.domain.security

class CheckAppLockUseCase(
    private val appLockRepository: AppLockRepository
) {
    fun invoke(): Boolean {
        return appLockRepository.checkIfAppLocked()
    }
}
