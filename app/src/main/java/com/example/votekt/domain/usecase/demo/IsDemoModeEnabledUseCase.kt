package com.example.votekt.domain.usecase.demo

import com.example.votekt.domain.repository.DemoModeRepository

class IsDemoModeEnabledUseCase(
    private val demoModeRepository: DemoModeRepository
) {
    fun invoke(): Boolean {
        return demoModeRepository.isDemoModeEnabled()
    }
}
