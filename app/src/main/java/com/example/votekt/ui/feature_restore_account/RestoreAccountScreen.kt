package com.example.votekt.ui.feature_restore_account

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.account.MnemonicWord
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.TipView
import com.example.votekt.ui.components.preview.MnemonicPreviewProvider
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.components.text.SeedPhraseInputField
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountIntent
import com.example.votekt.ui.feature_restore_account.model.RestoreAccountNavigationEvent
import com.example.votekt.ui.theme.Dimensions
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun RestoreAccountScreen(
    viewModel: RestoreAccountViewModel = koinViewModel(),
    onNavigationEvent: (RestoreAccountNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

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
    ScreenPreview() {
        RestoreAccountScreen_Ui(
            words = words.joinToString(" ") { it.value }
        )
    }
}