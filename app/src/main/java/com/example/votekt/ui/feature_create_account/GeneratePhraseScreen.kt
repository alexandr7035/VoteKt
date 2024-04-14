package com.example.votekt.ui.feature_create_account

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.ui.components.FlowRow
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.feature_create_account.model.GeneratePhraseNavigationEvent
import com.example.votekt.ui.feature_create_account.model.GenerateSeedIntent
import com.example.votekt.ui.feature_create_account.model.GenerateSeedState
import com.example.votekt.ui.theme.VoteKtTheme
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeneratePhraseScreen(
    viewModel: GeneratePhraseViewModel = koinViewModel(),
    onConfirm: (words: List<MnemonicWord>) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.emitIntent(GenerateSeedIntent.Load)
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value

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
            text = "#${word.index + 1}. ${word.value}",
            style = TextStyle(
                fontSize = 18.sp,
            ),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
@Preview
private fun GeneratePhraseScreen_Preview() {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            GeneratePhraseScreen_Ui(
                state = GenerateSeedState(
                    words = List(12) {
                        MnemonicWord(0, "Sample")
                    }
                )
            )
        }
    }
}