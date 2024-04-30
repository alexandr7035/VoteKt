package com.example.votekt.ui.feature_create_proposal

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.ui.app_host.host_utils.LocalScopedSnackbarState
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SelectorGroup
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.selector_group.SelectorOption
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenIntent
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenNavigationEvent
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenState
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.theme.VoteKtTheme
import de.palm.composestateevents.EventEffect
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
fun CreateProposalScreen(
    viewModel: CreateProposalViewModel = koinViewModel(),
    onNavigationEvent: (CreateProposalScreenNavigationEvent) -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarState = LocalScopedSnackbarState.current

    CreateProposalScreen_Ui(
        state = state,
        onIntent = viewModel::onIntent,
        isLoading = state.isLoading
    )

    EventEffect(
        event = state.errorEvent,
        onConsumed = viewModel::onProposalCreatedEvent
    ) { error ->
        snackbarState.show(error.asString(context))
    }

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = { /*TODO*/ }
    ) {
        onNavigationEvent(it)
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(CreateProposalScreenIntent.EnterScreen)
    }
}

@Composable
private fun CreateProposalScreen_Ui(
    state: CreateProposalScreenState,
    onIntent: (CreateProposalScreenIntent) -> Unit,
    isLoading: Boolean = false,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(R.string.new_proposal),
                onBack = {
                    onIntent(CreateProposalScreenIntent.BackClick)
                },
            )
        },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = pv.calculateTopPadding() + 8.dp,
                    bottom = pv.calculateBottomPadding(),
                    start = Dimensions.screenPaddingHorizontal,
                    end = Dimensions.screenPaddingVertical,
                )
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    maxLines = 1,
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= state.titleMaxLength) {
                            onIntent(CreateProposalScreenIntent.ChangeTitle(it))
                        } else {
                            onIntent(CreateProposalScreenIntent.ChangeTitle(it.take(state.titleMaxLength)))
                        }
                    },
                    label = { Text(stringResource(R.string.proposal_title)) }
                )

                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End),
                    text = stringResource(id = R.string.field_length_template, state.title.length, state.titleMaxLength),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    value = state.description,
                    onValueChange = {
                        if (it.length <= state.descMaxLength) {
                            onIntent(CreateProposalScreenIntent.ChangeDescription(it))
                        } else {
                            onIntent(CreateProposalScreenIntent.ChangeDescription(it.take(state.descMaxLength)))
                        }
                    },
                    label = { Text(stringResource(R.string.proposal_desc)) },
                )


                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End),
                    text = stringResource(id = R.string.field_length_template, state.description.length, state.descMaxLength),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.choose_proposal_duration),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                SelectorGroup(
                    onSelectedChanged = {
                        onIntent(CreateProposalScreenIntent.SelectProposalDuration(it.value))
                    },
                    options = listOf(
                        SelectorOption(1.hours, "1 hour"),
                        SelectorOption(24.hours, "24 hours"),
                        SelectorOption(7.days, "7 days"),
                        SelectorOption(30.days, "30 days"),
                    ),
                    fontSize = 14.sp,
                )
            }

            Box(
                modifier = Modifier.padding(
                    vertical = Dimensions.screenPaddingVertical,
                )
            ) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_proposal),
                    onClick = {
                        onIntent(CreateProposalScreenIntent.SubmitProposal)
                    },
                    enabled = state.isSaveButtonEnabled
                )
            }
        }
    }

    if (isLoading) {
        FullscreenProgressBar()
    }
}

@Preview
@Composable
fun CreateProposalScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
        CreateProposalScreen_Ui(
            state = CreateProposalScreenState(),
            onIntent = {},
        )
    }
}
