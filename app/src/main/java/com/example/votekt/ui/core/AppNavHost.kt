package com.example.votekt.ui.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.votekt.ui.app_host.graphs.createAppLockGraph
import com.example.votekt.ui.app_host.host_utils.LocalScopedSnackbarState
import com.example.votekt.ui.app_host.host_utils.ScopedSnackBarState
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.loading.LoadingScreen
import com.example.votekt.ui.components.snackbar.ResultSnackBar
import com.example.votekt.ui.create_proposal.CreateProposalScreen
import com.example.votekt.ui.feature_app_lock.lock_screen.LockScreen
import com.example.votekt.ui.feature_app_lock.lock_screen.LockScreenNavigationEvent
import com.example.votekt.ui.feature_confirm_transaction.ReviewTransactionDialog
import com.example.votekt.ui.feature_create_account.ConfirmPhraseScreen
import com.example.votekt.ui.feature_create_account.GeneratePhraseScreen
import com.example.votekt.ui.feature_create_account.welcome_screen.WelcomeScreen
import com.example.votekt.ui.feature_node_connection.NodeConnectionIntent
import com.example.votekt.ui.feature_node_connection.NodeConnectionScreen
import com.example.votekt.ui.feature_proposals.proposal_details.VotingDetailsScreen
import com.example.votekt.ui.feature_proposals.proposals_list.ProposalsScreen
import com.example.votekt.ui.feature_restore_account.RestoreAccountScreen
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountNavigationEvent
import com.example.votekt.ui.feature_wallet.WalletScreen
import com.example.votekt.ui.feature_wallet.model.WalletScreenNavigationEvent
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenNavigationEvent
import com.example.votekt.ui.tx_history.TransactionHistoryScreen
import org.koin.androidx.compose.koinViewModel

// TODO refactoring of this screen
@Composable
fun AppNavHost(
    viewModel: AppViewModel = koinViewModel(), navController: NavHostController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val hostCoroutineScope = rememberCoroutineScope()

    CompositionLocalProvider(
        LocalScopedSnackbarState provides ScopedSnackBarState(
            value = snackBarHostState,
            coroutineScope = hostCoroutineScope
        ),
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val showBottomNav = NavDestinations.Primary.routes().contains(navBackStackEntry?.destination?.route)

        val state = viewModel.appState.collectAsStateWithLifecycle().value

        when (state) {
            is AppState.Loading -> {
                LoadingScreen(null)
            }

            is AppState.InitFailure -> {
                ErrorFullScreen(error = state.error)
            }

            // TODO separate file
            is AppState.Ready -> {
                // Conditional navigation
                LaunchedEffect(Unit) {
                    if (state.conditionalNavigation.requireCreateAccount) {
                        navigateToWelcomeScreen(navController)
                    }

                    if (state.conditionalNavigation.requireCreateAppLock) {
                        navController.navigate(NavDestinations.SetupAppLockGraph.route) {
                            popUpTo(NavDestinations.Primary.Wallet.route) {
                                inclusive = true
                            }
                        }
                    }

                    if (state.conditionalNavigation.requireUnlockApp) {
                        navController.navigate(NavDestinations.AppLock.route)
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (showBottomNav) {
                            AppBottomNav(navController = navController)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState) { ResultSnackBar(snackbarData = it) }
                    }
                ) { pv ->

                    NavHost(
                        navController = navController,
                        startDestination = NavDestinations.Primary.Wallet.route,
                        modifier = Modifier.padding(bottom = pv.calculateBottomPadding())
                    ) {
                        createAppLockGraph(navController)

                        composable(NavDestinations.AppLock.route) {
                            LockScreen(
                                onNavigationEvent = {
                                    when (it) {
                                        LockScreenNavigationEvent.GoToWelcome -> {
                                            navigateToWelcomeScreen(navController)
                                        }
                                        LockScreenNavigationEvent.PopupBack -> {
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            )
                        }

                        composable(NavDestinations.Welcome.route) {
                            WelcomeScreen(
                                onNavigationEvent = {
                                    when (it) {
                                        WelcomeScreenNavigationEvent.ToCreateAccount -> {
                                            navController.navigate(NavDestinations.GeneratePhrase.route)
                                        }

                                        WelcomeScreenNavigationEvent.ToRestoreAccount -> {
                                            navController.navigate(NavDestinations.RestoreAccount.route)
                                        }
                                    }
                                }
                            )
                        }

                        composable(NavDestinations.RestoreAccount.route) {
                            RestoreAccountScreen(
                                onNavigationEvent = {
                                    when (it) {
                                        RestoreAccountNavigationEvent.GoToHome -> {
                                            navController.navigate(NavDestinations.Primary.Wallet.route) {
                                                popUpTo(NavDestinations.RestoreAccount.route) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable(NavDestinations.GeneratePhrase.route) {
                            GeneratePhraseScreen(onConfirm = { words ->
                                val phrase = words.joinToString(" ") {
                                    it.value
                                }

                                navController.navigate("${NavDestinations.ConfirmPhrase.route}/$phrase")
                            })
                        }

                        // TODO move logic out
                        composable(
                            route = "${NavDestinations.ConfirmPhrase.route}/{seedPhrase}", arguments = listOf(navArgument("seedPhrase") {
                                type = NavType.StringType
                            })
                        ) {
                            val phrase = it.arguments?.getString("seedPhrase")?.split(" ")?.mapIndexed { index, word ->
                                MnemonicWord(index, word)
                            }.orEmpty()

                            ConfirmPhraseScreen(phraseToConfirm = phrase, onConfirm = {
                                navController.navigate(NavDestinations.Primary.Wallet.route) {
                                    popUpTo(NavDestinations.ConfirmPhrase.route) {
                                        inclusive = true
                                    }
                                }
                            })
                        }

                        composable(NavDestinations.Primary.Wallet.route) {
                            WalletScreen(
                                onNavigationEvent = {
                                    when (it) {
                                        WalletScreenNavigationEvent.ToWelcomeScreen -> {
                                            navigateToWelcomeScreen(navController)
                                        }

                                        else -> {
                                            // TODO
                                        }
                                    }
                                }
                            )
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
                                onProposalCreated = { proposalId ->
                                    navController.navigate("${NavDestinations.VotingDetails.route}/${proposalId.value}")
                                },
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
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
                                onBack = {
                                    navController.popBackStack(
                                        route = NavDestinations.Primary.Proposals.route,
                                        inclusive = false
                                    )
                                }
                            )
                        }
                    }

                    if (state.requiresTxConfirmation()) {
                        ReviewTransactionDialog(
                            onIntent = viewModel::onTransactionIntent,
                            state = state.txConfirmationState!!,
                        )
                    }
                }
            }

            AppState.NodeConnectionError -> NodeConnectionScreen(
                onIntent = {
                    when (it) {
                        NodeConnectionIntent.TryAgain -> viewModel.onAppIntent(AppIntent.ReconnectToNode)
                    }
                }
            )
        }
    }

}

private fun navigateToWelcomeScreen(navController: NavHostController) {
    navController.navigate(NavDestinations.Welcome.route) {
        popUpTo(NavDestinations.Primary.Wallet.route) {
            inclusive = true
        }
    }
}