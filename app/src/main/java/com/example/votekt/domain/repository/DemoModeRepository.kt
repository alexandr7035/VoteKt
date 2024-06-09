package com.example.votekt.domain.repository

import com.example.votekt.domain.model.demo.DemoProposal

interface DemoModeRepository {
    fun isDemoModeEnabled(): Boolean
    fun getRandomDemoProposal(): DemoProposal
}
