package com.example.votekt.ui.feature_app_lock.lock_screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.core.OperationResult
import com.example.votekt.ui.app_host.host_utils.LocalScopedSnackbarState
import com.example.votekt.ui.asTextError
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.core.effects.OnResumeEffect
import com.example.votekt.ui.feature_app_lock.ui.AppLockScreenComponent
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult
import com.example.votekt.ui.feature_app_lock.ui.BiometricsHelper
import com.example.votekt.ui.utils.findActivity
import com.example.votekt.ui.utils.showToast
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
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
            val data = BiometricsHelper.buildBiometricsPrompt(
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

            viewModel.onIntent(LockScreenIntent.AuthenticateWithBiometrics(data))
        }
    }

    NavigationEventEffect(
        event = state.logoutEvent,
        onConsumed = viewModel::consumeLogoutEvent,
    ) { logoutResult ->
        when (logoutResult) {
            is OperationResult.Failure -> {
                snackBarState.show(
                    message = logoutResult.error.errorType.asTextError().asString(context),
                    snackBarMode = SnackBarMode.Negative
                )
            }

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