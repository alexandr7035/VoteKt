package com.example.votekt.core.config

import android.content.Context
import com.example.votekt.R
import kotlin.random.Random

object ProposalConfig {

    fun getRandomMockProposalText(context: Context): Pair<String, String> {
        val variants = listOf(
            Pair(
                context.getString(R.string.mock_proposal_title_1),
                context.getString(R.string.mock_proposal_desc_1)
            ),
            Pair(
                context.getString(R.string.mock_proposal_title_2),
                context.getString(R.string.mock_proposal_desc_2)
            ),
            Pair(
                context.getString(R.string.mock_proposal_title_3),
                context.getString(R.string.mock_proposal_desc_3)
            )
        )

        return variants.elementAt(Random.nextInt(0, variants.size))
    }
}