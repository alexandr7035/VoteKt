package com.example.votekt.ui.core

import androidx.annotation.DrawableRes
import com.example.votekt.R

// TODO refactoring of this
sealed class NavDestinations {
    sealed class Primary(
        val label: String,
        @DrawableRes val filledIcon: Int,
    ): NavDestinations() {
        object Wallet: Primary(
            filledIcon = R.drawable.ic_wallet,
            label = "Wallet"
        )

        object Proposals: Primary(
            filledIcon = R.drawable.ic_votings,
            label = "Proposals"
        )

        object Transactions: Primary(
            filledIcon = R.drawable.ic_transactions,
            label = "History"
        )

        companion object {
            fun all(): List<Primary> = listOf(Wallet, Proposals, Transactions)
            fun routes() = all().map { it.route }
        }
    }

    object AppLock: NavDestinations()

    object SetupAppLockGraph : NavDestinations() {
        object CreatePin : NavDestinations()
        object EnableBiometrics : NavDestinations()
    }

    object Welcome: NavDestinations()

    object RestoreAccount: NavDestinations()

    object GeneratePhrase: NavDestinations()
    object ConfirmPhrase: NavDestinations()

    object NewProposal: NavDestinations()

    object VotingDetails : NavDestinations()

    val route: String
        get() = this::class.java.simpleName
}

