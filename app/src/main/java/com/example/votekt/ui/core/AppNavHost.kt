package com.example.votekt.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.votekt.ui.VotingViewModel
import com.example.votekt.ui.voting_details.VotingDetailsScreen
import com.example.votekt.ui.votings_list.ProposalsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController) {

    val bottomNavItems = listOf(
        NavEntries.Proposals,
        NavEntries.Admin,
    )

    Scaffold(bottomBar = {
        BottomAppBar() {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            bottomNavItems.forEach { screen ->
                val isDestinationSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                // Bottom nav icons
                NavigationBarItem(
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    icon = {
                        Icon(
                            imageVector = screen.navIcon!!,
                            contentDescription = null,
                            tint = if (isDestinationSelected) Color.Red else Color.Gray
                        )
                    },
                    label = { Text(text = screen.label) },
                )
            }
        }
    }) { pv ->

        // FIXME DI
        val viewModel = remember { VotingViewModel() }

        NavHost(
            navController = navController,
            startDestination = NavEntries.Proposals.route,
            modifier = Modifier.padding(bottom = pv.calculateBottomPadding())
        ) {
            composable(NavEntries.Proposals.route) {
                ProposalsScreen(onProposalClick = {
                    navController.navigate(NavEntries.VotingDetails.route)
                })
            }

            composable(NavEntries.Admin.route) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "TODO", fontSize = 24.sp)
                }
            }

            composable(NavEntries.VotingDetails.route) {
                VotingDetailsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }


}