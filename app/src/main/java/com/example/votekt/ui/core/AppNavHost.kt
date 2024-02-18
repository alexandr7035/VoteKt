package com.example.votekt.ui.core

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.votekt.data.account.mnemonic.Word
import com.example.votekt.ui.components.snackbar.ResultSnackBar
import com.example.votekt.ui.components.snackbar.showResultSnackBar
import com.example.votekt.ui.create_proposal.CreateProposalScreen
import com.example.votekt.ui.feature_create_account.ConfirmPhraseScreen
import com.example.votekt.ui.feature_create_account.GeneratePhraseScreen
import com.example.votekt.ui.feature_wallet.WalletScreen
import com.example.votekt.ui.tx_history.TransactionHistoryScreen
import com.example.votekt.ui.voting_details.VotingDetailsScreen
import com.example.votekt.ui.votings_list.ProposalsScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomNav = NavDestinations.Primary.routes()
        .contains(navBackStackEntry?.destination?.route)

    val snackBarHostState = remember { SnackbarHostState() }
    val hostCoroutineScope = rememberCoroutineScope()

    Scaffold(bottomBar = {
        if (showBottomNav) {
            AppBottomNav(navController = navController)
        }
    }, snackbarHost = {
        SnackbarHost(hostState = snackBarHostState, snackbar = { ResultSnackBar(snackbarData = it) })
    }) { pv ->

        NavHost(
            navController = navController,
//            startDestination = NavDestinations.Primary.Wallet.route,
            startDestination = NavDestinations.GeneratePhrase.route,
            modifier = Modifier.padding(bottom = pv.calculateBottomPadding())
        ) {

            composable(NavDestinations.GeneratePhrase.route) {
                GeneratePhraseScreen(
                    onConfirm = { words ->
                        val phrase = words.map {
                            it.value
                        }.joinToString(" ")

                        navController.navigate("${NavDestinations.ConfirmPhrase.route}/$phrase")
                    }
                )
            }

            // TODO move logic out
            composable(
                route = "${NavDestinations.ConfirmPhrase.route}/{seedPhrase}",
                arguments = listOf(navArgument("seedPhrase") {
                    type = NavType.StringType
                })
            ) {
                val phrase = it.arguments?.getString("seedPhrase")
                    ?.split(" ")
                    ?.mapIndexed { index, word ->
                        Word(index, word)
                    }.orEmpty()

                ConfirmPhraseScreen(
                    phraseToConfirm = phrase,
                    onConfirm = {
                        navController.navigate(NavDestinations.Primary.Wallet.route) {
                            popUpTo(NavDestinations.ConfirmPhrase.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(NavDestinations.Primary.Wallet.route) {
                WalletScreen()
            }

            composable(NavDestinations.Primary.Proposals.route) {
                ProposalsScreen(onProposalClick = { proposalId ->
                    navController.navigate("${NavDestinations.VotingDetails.route}/${proposalId}")
                }, onNewProposalClick = {
                    navController.navigate(NavDestinations.NewProposal.route)
                })
            }

            composable(NavDestinations.NewProposal.route) {
                CreateProposalScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onShowSnackBar = { msg, mode ->
                        hostCoroutineScope.launch {
                            snackBarHostState.showResultSnackBar(msg, mode)
                        }
                    })
            }

            composable(NavDestinations.Primary.Transactions.route) {
                TransactionHistoryScreen()
            }

            composable(
                route = "${NavDestinations.VotingDetails.route}/{proposalId}",
                arguments = listOf(navArgument("proposalId") { type = NavType.LongType })
            ) {
                VotingDetailsScreen(
                    proposalId = it.arguments?.getLong("proposalId")!!,
                    onBack = { navController.popBackStack() },
                    onShowSnackBar = { msg, mode ->
                        hostCoroutineScope.launch {
                            snackBarHostState.showResultSnackBar(msg, mode)
                        }
                    })
            }
        }
    }


}