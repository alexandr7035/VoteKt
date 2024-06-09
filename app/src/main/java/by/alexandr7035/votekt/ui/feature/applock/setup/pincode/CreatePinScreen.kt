package by.alexandr7035.votekt.ui.feature.applock.setup.pincode

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.ui.feature.applock.core.AppLockScreenComponent
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
