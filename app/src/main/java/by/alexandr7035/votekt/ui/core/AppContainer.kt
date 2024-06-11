package by.alexandr7035.votekt.ui.core

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.ui.components.ErrorFullScreen
import by.alexandr7035.votekt.ui.components.loading.LoadingScreen
import by.alexandr7035.votekt.ui.components.snackbar.ResultSnackBar
import by.alexandr7035.votekt.ui.core.graphs.createAppLockGraph
import by.alexandr7035.votekt.ui.feature.account.create.ConfirmPhraseScreen
import by.alexandr7035.votekt.ui.feature.account.create.GeneratePhraseScreen
import by.alexandr7035.votekt.ui.feature.account.restore.RestoreAccountScreen
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountNavigationEvent
import by.alexandr7035.votekt.ui.feature.applock.lockscreen.LockScreen
import by.alexandr7035.votekt.ui.feature.applock.lockscreen.LockScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.onboarding.WelcomeScreen
import by.alexandr7035.votekt.ui.feature.onboarding.model.WelcomeScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.proposals.create.CreateProposalScreen
import by.alexandr7035.votekt.ui.feature.proposals.create.model.CreateProposalScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.proposals.details.ProposalDetailsScreen
import by.alexandr7035.votekt.ui.feature.proposals.details.ProposalDetailsScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.proposals.feed.ProposalsScreen
import by.alexandr7035.votekt.ui.feature.proposals.feed.ProposalsScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.transactions.history.TransactionHistoryScreen
import by.alexandr7035.votekt.ui.feature.transactions.history.model.TransactionsScreenNavigationEvent
import by.alexandr7035.votekt.ui.feature.transactions.review.ReviewTransactionDialog
import by.alexandr7035.votekt.ui.feature.wallet.WalletScreen
import by.alexandr7035.votekt.ui.feature.wallet.model.WalletScreenNavigationEvent
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.utils.compositionlocal.LocalScopedSnackbarState
import by.alexandr7035.votekt.ui.utils.compositionlocal.ScopedSnackBarState
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel

// TODO refactoring of this screen
@Composable
fun AppContainer(
    viewModel: AppViewModel = koinViewModel(),
    navController: NavHostController
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val hostCoroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    CompositionLocalProvider(
        LocalScopedSnackbarState provides ScopedSnackBarState(
            value = snackBarHostState, coroutineScope = hostCoroutineScope
        ),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val showBottomNav = NavDestinations.Primary.routes().contains(navBackStackEntry?.destination?.route)
        val state = viewModel.appState.collectAsStateWithLifecycle().value

        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    AppBottomNav(navController = navController)
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { ResultSnackBar(snackBarData = it) }
            }
        ) { pv ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = pv.calculateBottomPadding()
                    )
            ) {
                when {
                    state.isLoading -> {
                        LoadingScreen(null)
                    }

                    state.appError != null -> {
                        ErrorFullScreen(error = state.appError)
                    }

                    else -> {
                        ScreenContent(navController, state, viewModel)
                    }
                }
            }
        }

        EventEffect(
            event = state.openExplorerEvent,
            onConsumed = viewModel::consumeOpenExplorerEvent
        ) { url ->
            val intent: CustomTabsIntent = CustomTabsIntent.Builder()
                .build()
            intent.launchUrl(context, Uri.parse(url))
        }
    }
}

@Composable
private fun ScreenContent(
    navController: NavHostController,
    state: AppState,
    viewModel: AppViewModel
) {
    Column {
        Box(Modifier.weight(1f)) {
            AppNavHost(
                navController = navController,
                conditionalNav = state.conditionalNavigation,
                onAppUnlocked = {
                    viewModel.onAppIntent(AppIntent.ConsumeAppUnlocked)
                },
                modifier = Modifier.fillMaxSize(),
                onOpenExplorer = { payload, exploreType ->
                    viewModel.onAppIntent(
                        AppIntent.OpenBlockExplorer(
                            payload = payload,
                            exploreType = exploreType,
                        )
                    )
                }
            )
        }

        AnimatedVisibility(
            visible = state.isAppLocked().not() && state.isOffline() && state.isLoggedIn(),
            enter = slideInVertically(initialOffsetY = { it * 2 }),
            exit = slideOutVertically(targetOffsetY = { it * 2 }),
        ) {
            NoConnectionStub(
                onClick = {
                    viewModel.onAppIntent(
                        AppIntent.ReconnectToNode
                    )
                },
                appConnectionState = state.appConnectionState
            )
        }

        if (state.requiresTxConfirmation()) {
            ReviewTransactionDialog(
                onIntent = viewModel::onTransactionIntent,
                state = state.txConfirmationState!!,
                onBiometricAuthEventConsumed = viewModel::consumeBiometricTransactionConfirmationEvent
            )
        }
    }
}

@Composable
private fun NoConnectionStub(
    onClick: () -> Unit,
    appConnectionState: AppConnectionState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                horizontal = Dimensions.screenPaddingHorizontal,
                vertical = 8.dp
            )
            .clickable {
                if (appConnectionState == AppConnectionState.OFFLINE) {
                    onClick()
                }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (appConnectionState) {
                AppConnectionState.CONNECTING -> stringResource(R.string.connecting)
                // Not needed
                AppConnectionState.ONLINE -> ""
                AppConnectionState.OFFLINE -> stringResource(R.string.offline)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
private fun AppNavHost(
    navController: NavHostController,
    conditionalNav: ConditionalNavigation,
    onAppUnlocked: () -> Unit,
    onOpenExplorer: (payload: String, exploreType: ExploreType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(conditionalNav) {
        if (conditionalNav.requireCreateAccount) {
            navController.navigateToWelcomeScreen()
        }

        if (conditionalNav.requireCreateAppLock) {
            navController.navigate(NavDestinations.SetupAppLockGraph.route) {
                popUpTo(NavDestinations.Primary.Wallet.route) {
                    inclusive = true
                }
            }
        }

        if (conditionalNav.requireUnlockApp) {
            navController.navigate(NavDestinations.AppLock.route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavDestinations.Primary.Wallet.route,
        modifier = modifier,
    ) {
        createAppLockGraph(navController)

        composable(NavDestinations.AppLock.route) {
            LockScreen(onNavigationEvent = {
                when (it) {
                    LockScreenNavigationEvent.GoToWelcome -> {
                        navController.navigateToWelcomeScreen()
                    }

                    LockScreenNavigationEvent.PopupBack -> {
                        onAppUnlocked()
                        navController.popBackStack()
                    }
                }
            })
        }

        composable(NavDestinations.Welcome.route) {
            WelcomeScreen(onNavigationEvent = {
                when (it) {
                    WelcomeScreenNavigationEvent.ToCreateAccount -> {
                        navController.navigate(NavDestinations.GeneratePhrase.route)
                    }

                    WelcomeScreenNavigationEvent.ToRestoreAccount -> {
                        navController.navigate(NavDestinations.RestoreAccount.route)
                    }
                }
            })
        }

        composable(NavDestinations.RestoreAccount.route) {
            RestoreAccountScreen(onNavigationEvent = {
                when (it) {
                    RestoreAccountNavigationEvent.GoToSetupAppLock -> {
                        navController.navigate(NavDestinations.SetupAppLockGraph.route) {
                            popUpTo(NavDestinations.RestoreAccount.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            })
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
            route = "${NavDestinations.ConfirmPhrase.route}/{seedPhrase}",
            arguments = listOf(
                navArgument("seedPhrase") {
                    type = NavType.StringType
                }
            )
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
            WalletScreen(onNavigationEvent = {
                when (it) {
                    is WalletScreenNavigationEvent.ToWelcomeScreen -> {
                        navController.navigateToWelcomeScreen()
                    }

                    is WalletScreenNavigationEvent.ToExplorer -> {
                        onOpenExplorer(it.payload, it.exploreType)
                    }

                    else -> {
                        // TODO
                    }
                }
            })
        }

        composable(NavDestinations.Primary.Proposals.route) {
            ProposalsScreen(
                onNavigationEvent = {
                    when (it) {
                        is ProposalsScreenNavigationEvent.ToExplorer -> {
                            onOpenExplorer(it.payload, it.exploreType)
                        }

                        is ProposalsScreenNavigationEvent.ToProposal -> {
                            navController.navigate("${NavDestinations.VotingDetails.route}/${it.uuid}")
                        }

                        is ProposalsScreenNavigationEvent.ToAddProposal -> {
                            navController.navigate(NavDestinations.NewProposal.route)
                        }
                    }
                },
            )
        }

        composable(NavDestinations.NewProposal.route) {
            CreateProposalScreen(
                onNavigationEvent = {
                    when (it) {
                        is CreateProposalScreenNavigationEvent.NavigateToProposal -> {
                            navController.navigate("${NavDestinations.VotingDetails.route}/${it.proposalId.value}")
                        }
                        is CreateProposalScreenNavigationEvent.PopupBack -> {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(NavDestinations.Primary.Transactions.route) {
            TransactionHistoryScreen(
                onNavigationEvent = {
                    when (it) {
                        is TransactionsScreenNavigationEvent.ToExplorer -> {
                            onOpenExplorer(it.payload, it.exploreType)
                        }
                    }
                }
            )
        }

        composable(
            route = "${NavDestinations.VotingDetails.route}/{proposalId}",
            arguments = listOf(navArgument("proposalId") { type = NavType.StringType })
        ) {
            ProposalDetailsScreen(
                proposalId = it.arguments?.getString("proposalId")!!,
                onNavigationEvent = {
                    when (it) {
                        is ProposalDetailsScreenNavigationEvent.PopupBack -> {
                            navController.popBackStack(
                                route = NavDestinations.Primary.Proposals.route,
                                inclusive = false
                            )
                        }
                        is ProposalDetailsScreenNavigationEvent.ToExplorer -> {
                            onOpenExplorer(it.payload, it.exploreType)
                        }
                    }
                }
            )
        }
    }
}

private fun NavController.navigateToWelcomeScreen() {
    navigate(NavDestinations.Welcome.route) {
        popUpTo(NavDestinations.Primary.Wallet.route) {
            inclusive = true
        }
    }
}
