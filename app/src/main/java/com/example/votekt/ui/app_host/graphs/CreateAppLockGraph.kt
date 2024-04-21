package com.example.votekt.ui.app_host.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.votekt.ui.feature_app_lock.setup_applock.biometrics.EnableBiometricsScreen
import com.example.votekt.ui.feature_app_lock.setup_applock.create_pin.CreatePinScreen
import com.example.votekt.ui.core.NavDestinations

private val targetRoot = NavDestinations.Primary.Wallet.route

fun NavGraphBuilder.createAppLockGraph(
    navController: NavController
) {
    navigation(
        route = NavDestinations.SetupAppLockGraph.route,
        startDestination = NavDestinations.SetupAppLockGraph.CreatePin.route
    ) {
        val thisGraph = NavDestinations.SetupAppLockGraph.route

        composable(NavDestinations.SetupAppLockGraph.CreatePin.route) {
            CreatePinScreen(
                onPinCreated = { result ->
                    if (result.shouldRequestBiometrics) {
                        navController.navigate("${NavDestinations.SetupAppLockGraph.EnableBiometrics.route}/${result.pin}") {
                            popUpTo(thisGraph) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(targetRoot) {
                            popUpTo(thisGraph) {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

        composable(
            route = "${NavDestinations.SetupAppLockGraph.EnableBiometrics.route}/{pinToEncrypt}",
            arguments = listOf(navArgument("pinToEncrypt") {
                type = NavType.StringType
            })
        ) {
            EnableBiometricsScreen(
                pinToEncrypt = it.arguments?.getString("pinToEncrypt")!!,
                onExit = {
                    navController.navigate(targetRoot) {
                        popUpTo(thisGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}