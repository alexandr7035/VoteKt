package com.example.votekt.domain.usecase.demo

import com.example.votekt.domain.model.demo.DemoProposal
import com.example.votekt.domain.repository.DemoModeRepository

class GetDemoProposalUseCase(
    private val demoModeRepository: DemoModeRepository
) {
    fun invoke(): DemoProposal {
        return demoModeRepository.getRandomDemoProposal()
    }
}
