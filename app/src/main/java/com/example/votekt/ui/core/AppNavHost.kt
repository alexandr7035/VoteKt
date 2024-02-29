package com.example.votekt.ui.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.progress.FullscreenProgressBar
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    viewModel: AppViewModel = koinViewModel(),
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomNav = NavDestinations.Primary.routes()
        .contains(navBackStackEntry?.destination?.route)

    val snackBarHostState = remember { SnackbarHostState() }
    val hostCoroutineScope = rememberCoroutineScope()

    val state = viewModel.appState.collectAsStateWithLifecycle().value

    when (state) {
        is AppState.Loading -> {
            FullscreenProgressBar()
        }

        is AppState.InitFailure -> {
            ErrorFullScreen(error = state.error)
        }

        // TODO separate file
        is AppState.Ready -> {
            // Conditional navigation
            LaunchedEffect(Unit) {
                if (state.conditionalNavigation.requireCreateAccount) {
                    navController.navigate(NavDestinations.GeneratePhrase.route) {
                        popUpTo(NavDestinations.Primary.Wallet.route) {
                            inclusive = true
                        }
                    }
                }
            }

            Scaffold(bottomBar = {
                if (showBottomNav) {
                    AppBottomNav(navController = navController)
                }
            }, snackbarHost = {
                SnackbarHost(hostState = snackBarHostState, snackbar = { ResultSnackBar(snackbarData = it) })
            }) { pv ->

                NavHost(
                    navController = navController,
                    startDestination = NavDestinations.Primary.Wallet.route,
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
                                MnemonicWord(index, word)
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
                        arguments = listOf(navArgument("proposalId") { type = NavType.StringType })
                    ) {
                        VotingDetailsScreen(
                            proposalId = it.arguments?.getString("proposalId")!!,
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
    }
}