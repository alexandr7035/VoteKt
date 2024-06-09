package by.alexandr7035.votekt.ui.feature.applock.lockscreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.core.OperationResult
import by.alexandr7035.votekt.ui.asTextError
import by.alexandr7035.votekt.ui.components.snackbar.SnackBarMode
import by.alexandr7035.votekt.ui.core.effects.OnResumeEffect
import by.alexandr7035.votekt.ui.feature.applock.core.AppLockScreenComponent
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricAuthResult
import by.alexandr7035.votekt.ui.feature.applock.core.BiometricsHelper
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.utils.compositionlocal.LocalScopedSnackbarState
import by.alexandr7035.votekt.ui.utils.findActivity
import by.alexandr7035.votekt.ui.utils.showToast
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LockScreen(
    viewModel: LockScreenViewModel = koinViewModel(),
    onNavigationEvent: (LockScreenNavigationEvent) -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val snackBarState = LocalScopedSnackbarState.current

    BackHandler(true) {
        context.showToast(R.string.please_unlock_the_app)
    }

    Scaffold(
        topBar = {
            LockScreenAppBar(viewModel)
        }
    ) {
        AppLockScreenComponent(
            state = state.uiState,
            onPinValueChange = {
                viewModel.onIntent(LockScreenIntent.PinFieldChange(it))
            },
        ) {
            viewModel.onIntent(LockScreenIntent.BiometricsBtnClicked)
        }
    }

    NavigationEventEffect(
        event = state.appUnlockEvent,
        onConsumed = viewModel::consumeUnlockWithPinEvent
    ) {
        onNavigationEvent(LockScreenNavigationEvent.PopupBack)
    }

    NavigationEventEffect(
        event = state.showBiometricsPromptEvent,
        onConsumed = viewModel::consumeShowBiometricsPromptEvent
    ) {
        val activity = context.findActivity()
        activity?.let {
            val data = buildBiometricPrompt(it, state, viewModel)
            viewModel.onIntent(LockScreenIntent.AuthenticateWithBiometrics(data))
        }
    }

    NavigationEventEffect(
        event = state.logoutEvent,
        onConsumed = viewModel::consumeLogoutEvent,
    ) { logoutResult ->
        when (logoutResult) {
            is OperationResult.Failure -> snackBarState.show(
                message = logoutResult.error.errorType.asTextError().asString(context),
                snackBarMode = SnackBarMode.Negative
            )

            is OperationResult.Success -> {
                onNavigationEvent(LockScreenNavigationEvent.GoToWelcome)
            }
        }
    }

    OnResumeEffect {
        viewModel.onIntent(
            LockScreenIntent.RefreshBiometricsAvailability
        )
    }
}

private fun buildBiometricPrompt(
    activity: FragmentActivity,
    state: LockScreenState,
    viewModel: LockScreenViewModel
) = BiometricsHelper.buildBiometricsPrompt(
    activity = activity,
    prompt = state.biometricsPromptState,
    onError = {
        viewModel.onIntent(
            LockScreenIntent.ConsumeBiometricAuthResult(
                BiometricAuthResult.Failure(it)
            )
        )
    },
    onSuccess = {
        viewModel.onIntent(
            LockScreenIntent.ConsumeBiometricAuthResult(
                BiometricAuthResult.Success(it)
            )
        )
    }
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LockScreenAppBar(viewModel: LockScreenViewModel) {
    TopAppBar(
        title = {},
        actions = {
            IconButton(
                onClick = {
                    viewModel.onIntent(LockScreenIntent.LogOut)
                }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_logout),
                    stringResource(id = R.string.log_out),
                    modifier = Modifier.size(Dimensions.appBarActionIconSize)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}
