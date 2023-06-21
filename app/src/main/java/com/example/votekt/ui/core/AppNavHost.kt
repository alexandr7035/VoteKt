package com.example.votekt.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.votekt.ui.voting_details.VotingDetailsScreen
import com.example.votekt.ui.votings_list.ProposalsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController, showBottomNav: Boolean = false
) {
    Scaffold(bottomBar = {
        if (showBottomNav) {
            AppBottomNav(navController = navController)
        }
    }) { pv ->

        NavHost(
            navController = navController,
            startDestination = NavEntries.Proposals.route,
            modifier = Modifier.padding(bottom = pv.calculateBottomPadding())
        ) {
            composable(NavEntries.Proposals.route) {
                ProposalsScreen(onProposalClick = { proposalId ->
                    navController.navigate("${NavEntries.VotingDetails.route}/${proposalId}")
                })
            }

            composable(NavEntries.Admin.route) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "TODO", fontSize = 24.sp)
                }
            }

            composable(
                route = "${NavEntries.VotingDetails.route}/{proposalId}",
                arguments = listOf(navArgument("proposalId") { type = NavType.StringType })
            ) {
                VotingDetailsScreen(
                    proposalId = it.arguments?.getString("proposalId")!!,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }


}