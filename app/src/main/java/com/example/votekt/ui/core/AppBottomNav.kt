package com.example.votekt.ui.core

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNav(navController: NavHostController) {

    val bottomNavItems = listOf(
        NavEntries.Wallet,
        NavEntries.Proposals,
        NavEntries.TxHistory,
    )

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
}