package com.example.votekt.ui.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.ui.graphics.vector.ImageVector

// TODO refactoring of this
sealed class NavEntries(
    val route: String,
    val navIcon: ImageVector?,
    val label: String
) {
    object Wallet: NavEntries(
        route = "wallet",
        navIcon = Icons.Filled.Email,
        label = "Wallet"
    )

    object Proposals : NavEntries(
        route = "proposals",
        navIcon = Icons.Filled.List,
        label = "Proposals"
    )

    object NewProposal: NavEntries(
        route = "new_proposal",
        navIcon = null,
        label = "New proposal"
    )

    object VotingDetails : NavEntries(
        route = "voting",
        navIcon = null,
        label = "Voting details"
    )

    object TxHistory: NavEntries(
        route = "tx_history",
        navIcon = Icons.Outlined.AccountBox,
        label = "Transactions"
    )

    object Admin : NavEntries(
        route = "admin",
        navIcon = Icons.Filled.Settings,
        label = "Admin"
    )
}
