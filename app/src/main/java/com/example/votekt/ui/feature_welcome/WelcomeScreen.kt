package com.example.votekt.ui.feature_create_account.welcome_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SecondaryButton
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.feature_welcome.WelcomeScreenViewModel
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenIntent
import com.example.votekt.ui.feature_welcome.model.WelcomeScreenNavigationEvent
import com.example.votekt.ui.theme.Dimensions
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel = koinViewModel(),
    onNavigationEvent: (WelcomeScreenNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    WelcomeScreen_Ui(
        onIntent = {
            viewModel.onIntent(it)
        }
    )

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent
    ) {
        onNavigationEvent(it)
    }
}

@Composable
private fun WelcomeScreen_Ui(
    onIntent: (WelcomeScreenIntent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimensions.screenPaddingHorizontal,
                vertical = Dimensions.screenPaddingVertical,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_to_app),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.create_account),
            onClick = {
                onIntent(WelcomeScreenIntent.CreateAccountClick)
            }
        )

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.already_have_account),
            onClick = {
                onIntent(WelcomeScreenIntent.AlreadyHaveAccountClick)
            }
        )
    }
}

@Preview
@Composable
private fun WelcomeScreen_Preview() {
    ScreenPreview {
        WelcomeScreen_Ui()
    }
}
