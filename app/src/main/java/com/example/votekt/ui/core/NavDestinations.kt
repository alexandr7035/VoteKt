package com.example.votekt.ui.core

import androidx.annotation.DrawableRes
import com.example.votekt.R

// TODO refactoring of this
sealed class NavDestinations(
    open val route: String,
) {
    sealed class Primary(
        override val route: String,
        val label: String,
        @DrawableRes val filledIcon: Int,
        @DrawableRes val outlinedIcon: Int
    ): NavDestinations(route) {
        object Wallet: Primary(
            route = "wallet",
            filledIcon = R.drawable.ic_wallet,
            outlinedIcon = R.drawable.ic_wallet,
            label = "Wallet"
        )

        object Proposals: Primary(
            route = "proposals",
            filledIcon = R.drawable.ic_votings,
            outlinedIcon = R.drawable.ic_vote_outlined,
            label = "Proposals"
        )

        object Transactions: Primary(
            route = "transactions",
            filledIcon = R.drawable.ic_votings,
            outlinedIcon = R.drawable.ic_vote_outlined,
            label = "History"
        )

        companion object {
            fun all(): List<Primary> = listOf(Wallet, Proposals, Transactions)
            fun routes() = all().map { it.route }
        }
    }

    object GeneratePhrase: NavDestinations(
        route = "generate_phrase"
    )
    object ConfirmPhrase: NavDestinations(
        route = "confirm_phrase"
    )

    object NewProposal: NavDestinations(
        route = "new_proposal",
    )

    object VotingDetails : NavDestinations(
        route = "voting",
    )
}

