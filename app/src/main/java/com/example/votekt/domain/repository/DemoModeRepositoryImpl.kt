package com.example.votekt.domain.repository

import android.content.Context
import com.example.votekt.BuildConfig
import com.example.votekt.R
import com.example.votekt.domain.model.demo_mode.DemoProposal
import kotlin.random.Random

class DemoModeRepositoryImpl(
    private val context: Context,
) : DemoModeRepository {
    override fun isDemoModeEnabled(): Boolean {
        return BuildConfig.ENABLE_DEMO_NODE
    }

    override fun getRandomDemoProposal(): DemoProposal {
        val variants = listOf(
            DemoProposal(
                context.getString(R.string.mock_proposal_title_1),
                context.getString(R.string.mock_proposal_desc_1)
            ),
            DemoProposal(
                context.getString(R.string.mock_proposal_title_2),
                context.getString(R.string.mock_proposal_desc_2)
            ),
            DemoProposal(
                context.getString(R.string.mock_proposal_title_3),
                context.getString(R.string.mock_proposal_desc_3)
            )
        )

        return variants.elementAt(Random.nextInt(0, variants.size))
    }
}