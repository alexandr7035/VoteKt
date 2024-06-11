package by.alexandr7035.votekt.ui.feature.account.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.model.account.MnemonicWord
import by.alexandr7035.votekt.domain.model.account.MnemonicWordConfirm
import by.alexandr7035.votekt.ui.components.PrimaryButton
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.preview.WordsToConfirmPreviewProvider
import by.alexandr7035.votekt.ui.components.selector.SelectorGroup
import by.alexandr7035.votekt.ui.components.selector.SelectorOption
import by.alexandr7035.votekt.ui.core.effects.ComposableLifeCycleEffect
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseIntent
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseNavigationEvent
import by.alexandr7035.votekt.ui.feature.account.create.model.ConfirmPhraseState
import by.alexandr7035.votekt.ui.utils.findActivity
import by.alexandr7035.votekt.ui.utils.lockScreenshots
import by.alexandr7035.votekt.ui.utils.unlockScreenshots
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmPhraseScreen(
    phraseToConfirm: List<MnemonicWord>,
    viewModel: ConfirmPhraseViewModel = koinViewModel(),
    onConfirm: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val activity = LocalContext.current.findActivity()

    LaunchedEffect(Unit) {
        viewModel.emitIntent(
            ConfirmPhraseIntent.LoadData(phrase = phraseToConfirm)
        )
    }

    ConfirmPhraseScreen_Ui(
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
            ConfirmPhraseNavigationEvent.ToHome -> onConfirm()
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
private fun ConfirmPhraseScreen_Ui(
    state: ConfirmPhraseState,
    onIntent: (ConfirmPhraseIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.confirm_phrase),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Spacer(Modifier.height(16.dp))

        state.confirmData.forEach { confirmationData ->
            Text(
                text = stringResource(
                    id = R.string.word_template,
                    confirmationData.rightWordIndex() + 1
                )
            )

            val shuffledOptions = remember(confirmationData) {
                confirmationData.shuffledWords()
            }

            RandomWordsGroup(onIntent, confirmationData, shuffledOptions)
        }

        Spacer(Modifier.weight(1f))

        state.confirmationError?.let {
            Text(
                text = it,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        PrimaryButton(
            text = stringResource(id = R.string.confirm),
            onClick = {
                onIntent(ConfirmPhraseIntent.ConfirmSeed)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RandomWordsGroup(
    onIntent: (ConfirmPhraseIntent) -> Unit,
    confirmationData: MnemonicWordConfirm,
    shuffledOptions: List<MnemonicWord>
) {
    SelectorGroup(
        onSelectedChanged = {
            onIntent(
                ConfirmPhraseIntent.SelectWordToConfirm(
                    index = confirmationData.rightWordIndex(),
                    word = it.value
                )
            )
        },
        options = shuffledOptions.map { word ->
            SelectorOption(
                value = word,
                valueText = word.value
            )
        }
    )
}

@Composable
@Preview
private fun GeneratedPhraseScreen_Preview(
    @PreviewParameter(WordsToConfirmPreviewProvider::class) wordsToConfirm: MnemonicWordConfirm
) {
    ScreenPreview {
        ConfirmPhraseScreen_Ui(
            state = ConfirmPhraseState(confirmData = listOf(wordsToConfirm))
        )
    }
}
