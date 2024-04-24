package com.example.votekt.ui.feature_app_lock.setup_applock.create_pin

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.ui.feature_app_lock.ui.AppLockScreenComponent
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreatePinScreen(
    viewModel: CreatePinViewModel = koinViewModel(),
    onPinCreated: (pinCreatedResult: PinCreatedResult) -> Unit = {}
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    AppLockScreenComponent(
        state = state.uiState,
        onPinValueChange = {
            viewModel.onIntent(CreatePinIntent.PinFieldChange(it))
        },
    )

    EventEffect(
        event = state.pinCreatedEvent,
        onConsumed = viewModel::consumePinCreatedEvent,
    ) { pinCreatedResult ->
        onPinCreated(pinCreatedResult)
    }
}