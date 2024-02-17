package com.example.votekt.ui.feature_create_account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.data.account.mnemonic.WordToConfirm
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SelectorGroup
import com.example.votekt.ui.components.selector_group.SelectorOption
import com.example.votekt.ui.feature_create_account.model.ConfirmPhraseState
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.mock
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConfirmPhraseScreen(
    viewModel: ConfirmPhraseViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    ConfirmPhraseScreen_Ui(state = state)
}

@Composable
private fun ConfirmPhraseScreen_Ui(
    state: ConfirmPhraseState,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        state.confirmData.forEach { confirmationData ->
            Text(
                text = "Word #${confirmationData.rightWordIndex() + 1}"
            )

            SelectorGroup(
                onSelectedChanged = {
                    // TODO
                },
                options = confirmationData.shuffledWords().map { word ->
                    SelectorOption(
                        value = word,
                        valueText = word.value
                    )
                }
            )
        }

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = "Confirm",
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
@Preview
private fun GeneratedPhraseScreen_Preview() {
    VoteKtTheme {
        val data = List(3) {
            WordToConfirm.mock()
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