package by.alexandr7035.votekt.ui.feature.account.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.ui.components.FlowRow
import by.alexandr7035.votekt.ui.components.PrimaryButton
import by.alexandr7035.votekt.ui.components.TipType
import by.alexandr7035.votekt.ui.components.TipView
import by.alexandr7035.votekt.ui.core.effects.ComposableLifeCycleEffect
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.account.create.model.GeneratePhraseNavigationEvent
import by.alexandr7035.votekt.ui.feature.account.create.model.GenerateSeedIntent
import by.alexandr7035.votekt.ui.feature.account.create.model.GenerateSeedState
import by.alexandr7035.votekt.ui.theme.VoteKtTheme
import by.alexandr7035.votekt.ui.utils.findActivity
import by.alexandr7035.votekt.ui.utils.lockScreenshots
import by.alexandr7035.votekt.ui.utils.unlockScreenshots
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeneratePhraseScreen(
    viewModel: GeneratePhraseViewModel = koinViewModel(),
    onConfirm: (words: List<MnemonicWord>) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(Unit) {
        viewModel.emitIntent(GenerateSeedIntent.Load)
    }
    GeneratePhraseScreen_Ui(
        state = state,
        onIntent = {
            viewModel.emitIntent(it)
        }
    )

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent
    ) {
        when (it) {
            GeneratePhraseNavigationEvent.ToConfirmPhrase -> onConfirm(state.words)
        }
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
private fun GeneratePhraseScreen_Ui(
    state: GenerateSeedState,
    onIntent: (GenerateSeedIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_to_app),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Spacer(Modifier.height(32.dp))

        FlowRow(
            verticalGap = 8.dp,
            horizontalGap = 12.dp,
            alignment = Alignment.CenterHorizontally
        ) {
            state.words.forEach {
                WordItem(word = it)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TipView(
            text = UiText.StringResource(R.string.seed_phrase_save_explanation),
            modifier = Modifier.fillMaxWidth(),
            tipType = TipType.WARMING,
        )

        Spacer(Modifier.height(12.dp))

        PrimaryButton(
            text = stringResource(R.string.i_ve_written_phrase),
            onClick = { onIntent(GenerateSeedIntent.Confirm) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WordItem(word: MnemonicWord) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(
                vertical = 12.dp,
                horizontal = 8.dp
            )
            .wrapContentSize()
    ) {
        Text(
            text = stringResource(
                id = R.string.mnemonic_word_indexed_template,
                word.index + 1,
                word.value
            ),
            style = TextStyle(
                fontSize = 16.sp,
            ),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview
private fun GeneratePhraseScreen_Preview() {
    VoteKtTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            GeneratePhraseScreen_Ui(
                state = GenerateSeedState(
                    words = List(12) {
                        MnemonicWord(it, "Sample")
                    }
                )
            )
        }
    }
}
