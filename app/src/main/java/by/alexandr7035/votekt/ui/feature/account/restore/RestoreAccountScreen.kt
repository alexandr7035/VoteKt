package by.alexandr7035.votekt.ui.feature.account.restore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.account.MnemonicWord
import by.alexandr7035.votekt.ui.components.PrimaryButton
import by.alexandr7035.votekt.ui.components.TipView
import by.alexandr7035.votekt.ui.components.preview.MnemonicPreviewProvider
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.text.SeedPhraseInputField
import by.alexandr7035.votekt.ui.core.effects.ComposableLifeCycleEffect
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountIntent
import by.alexandr7035.votekt.ui.feature.account.restore.model.RestoreAccountNavigationEvent
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.utils.findActivity
import by.alexandr7035.votekt.ui.utils.lockScreenshots
import by.alexandr7035.votekt.ui.utils.unlockScreenshots
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun RestoreAccountScreen(
    viewModel: RestoreAccountViewModel = koinViewModel(),
    onNavigationEvent: (RestoreAccountNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val activity = LocalContext.current.findActivity()

    RestoreAccountScreen_Ui(
        words = state.wordInput,
        onIntent = {
            viewModel.onIntent(it)
        },
        error = state.error
    )

    LaunchedEffect(Unit) {
        viewModel.onIntent(RestoreAccountIntent.EnterScreen)
    }

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent,
    ) {
        onNavigationEvent(it)
    }

    ComposableLifeCycleEffect { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                activity?.lockScreenshots()
            }

            Lifecycle.Event.ON_PAUSE -> {
                activity?.unlockScreenshots()
            }

            else -> {}
        }
    }
}

@Composable
private fun RestoreAccountScreen_Ui(
    words: String,
    onIntent: (RestoreAccountIntent) -> Unit = {},
    error: UiText? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = Dimensions.screenPaddingHorizontal,
                vertical = Dimensions.screenPaddingVertical,
            ),
    ) {
        Text(
            text = stringResource(R.string.enter_your_recovery_phrase),
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        )

        Spacer(Modifier.height(24.dp))

        TipView(
            text = UiText.StringResource(R.string.seed_phrase_restore_explanation),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.weight(1f))

        SeedPhraseInputField(
            value = words,
            onValueChange = {
                onIntent(RestoreAccountIntent.ChangePhrase(it))
            },
            error = error,
        )

        Spacer(Modifier.weight(1f))

        Spacer(Modifier.height(12.dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.confirm),
            onClick = { onIntent.invoke(RestoreAccountIntent.ConfirmClick) }
        )
    }
}

@Preview
@Composable
private fun RestoreAccountScreen_Preview(
    @PreviewParameter(MnemonicPreviewProvider::class) words: List<MnemonicWord>
) {
    ScreenPreview {
        RestoreAccountScreen_Ui(
            words = words.joinToString(" ") { it.value }
        )
    }
}
