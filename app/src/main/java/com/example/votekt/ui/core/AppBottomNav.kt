package com.example.votekt.ui.core

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.votekt.ui.components.bottomnav.M2BottomNavigation
import com.example.votekt.ui.components.bottomnav.M2BottomNavigationItem

@Composable
fun AppBottomNav(navController: NavHostController) {
    M2BottomNavigation(
        modifier = Modifier.height(72.dp),
        backgroundColor = MaterialTheme.colorScheme.surface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        NavDestinations.Primary.all().forEach { screen ->
            val isDestinationSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            // Bottom nav icons
            M2BottomNavigationItem(
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
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(24.dp),
                        painter = getDestinationIcon(isDestinationSelected, screen),
                        contentDescription = null,
                        // TODO
                        tint = if (isDestinationSelected) Color.Black else Color.Gray
                    )
                },
                label = { Text(text = screen.label) },
            )
        }
    }
}

@Composable
private fun getDestinationIcon(isSelected: Boolean, destination: NavDestinations.Primary): Painter {
    return if (isSelected) {
        painterResource(id = destination.filledIcon)
    }
    else {
        painterResource(id = destination.outlinedIcon)
    }
}