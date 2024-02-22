package com.example.votekt.ui.feature_create_account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.domain.account.MnemonicWordConfirm
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SelectorGroup
import com.example.votekt.ui.components.selector_group.SelectorOption
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseIntent
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseNavigationEvent
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseState
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.mock
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmPhraseScreen(
    phraseToConfirm: List<MnemonicWord>,
    viewModel: ConfirmPhraseViewModel = koinViewModel(),
    onConfirm: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.emitIntent(
            ConfirmPhraseIntent.LoadData(phrase = phraseToConfirm)
        )
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value
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
            text = "Confirm phrase",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        Spacer(Modifier.height(16.dp))

        state.confirmData.forEach { confirmationData ->
            Text(
                text = "Word #${confirmationData.rightWordIndex() + 1}"
            )

            val shuffledOptions = remember(confirmationData) {
                confirmationData.shuffledWords()
            }

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
            text = "Confirm",
            onClick = {
                onIntent(ConfirmPhraseIntent.ConfirmSeed)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
private fun GeneratedPhraseScreen_Preview() {
    VoteKtTheme {
        val data = List(3) {
            MnemonicWordConfirm.mock()
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConfirmPhraseScreen_Ui(
                state = ConfirmPhraseState(confirmData = data)
            )
        }
    }
}