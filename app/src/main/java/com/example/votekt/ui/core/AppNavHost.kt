package com.example.votekt.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.votekt.ui.components.snackbar.ResultSnackBar
import com.example.votekt.ui.components.snackbar.showResultSnackBar
import com.example.votekt.ui.tx_history.TransactionHistoryScreen
import com.example.votekt.ui.voting_details.VotingDetailsScreen
import com.example.votekt.ui.create_proposal.CreateProposalScreen
import com.example.votekt.ui.votings_list.ProposalsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController
) {

    val primaryDestinations = listOf(
        NavEntries.Proposals.route, NavEntries.TxHistory.route
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomNav = primaryDestinations.contains(navBackStackEntry?.destination?.route)

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
            startDestination = NavEntries.Proposals.route,
            modifier = Modifier.padding(bottom = pv.calculateBottomPadding())
        ) {
            composable(NavEntries.Proposals.route) {
                ProposalsScreen(onProposalClick = { proposalId ->
                    navController.navigate("${NavEntries.VotingDetails.route}/${proposalId}")
                }, onNewProposalClick = {
                    navController.navigate(NavEntries.NewProposal.route)
                })
            }

            composable(NavEntries.NewProposal.route) {
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

            composable(NavEntries.TxHistory.route) {
                TransactionHistoryScreen()
            }

            composable(NavEntries.Admin.route) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "TODO", fontSize = 24.sp)
                }
            }

            composable(
                route = "${NavEntries.VotingDetails.route}/{proposalId}",
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