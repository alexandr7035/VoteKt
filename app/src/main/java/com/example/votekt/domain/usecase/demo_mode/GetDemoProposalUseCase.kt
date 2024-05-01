package com.example.votekt.domain.usecase.demo_mode

import com.example.votekt.domain.model.demo_mode.DemoProposal
import com.example.votekt.domain.repository.DemoModeRepository

class GetDemoProposalUseCase(
    private val demoModeRepository: DemoModeRepository
) {
    fun invoke(): DemoProposal {
        return demoModeRepository.getRandomDemoProposal()
    }
}