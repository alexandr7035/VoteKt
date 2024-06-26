package by.alexandr7035.votekt.ui.core.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import by.alexandr7035.votekt.ui.core.NavDestinations
import by.alexandr7035.votekt.ui.feature.applock.setup.biometrics.EnableBiometricsScreen
import by.alexandr7035.votekt.ui.feature.applock.setup.pincode.CreatePinScreen

private val targetRoot = NavDestinations.Primary.Wallet.route
private val thisGraph = NavDestinations.SetupAppLockGraph.route

// This is not secure, consider using better approach
private const val ARG_PIN = "pinToEncrypt"

fun NavGraphBuilder.createAppLockGraph(
    navController: NavController
) {
    navigation(
        route = NavDestinations.SetupAppLockGraph.route,
        startDestination = NavDestinations.SetupAppLockGraph.CreatePin.route
    ) {
        composable(NavDestinations.SetupAppLockGraph.CreatePin.route) {
            CreatePinScreen(
                onPinCreated = { result ->
                    if (result.shouldRequestBiometrics) {
                        navController.navigate(
                            "${NavDestinations.SetupAppLockGraph.EnableBiometrics.route}/${result.pin}"
                        ) {
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
            route = "${NavDestinations.SetupAppLockGraph.EnableBiometrics.route}/{$ARG_PIN}",
            arguments = listOf(
                navArgument(ARG_PIN) {
                    type = NavType.StringType
                }
            )
        ) {
            EnableBiometricsScreen(
                pinToEncrypt = it.arguments?.getString(ARG_PIN)!!,
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
