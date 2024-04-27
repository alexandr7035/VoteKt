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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.BuildConfig
import com.example.votekt.R
import com.example.votekt.core.config.ProposalConfig
import com.example.votekt.domain.core.Uuid
import com.example.votekt.domain.model.contract.CreateDraftProposal
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SelectorGroup
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.selector_group.SelectorOption
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.feature_create_proposal.model.CreateProposalScreenIntent
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.showToast
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
fun CreateProposalScreen(
    viewModel: CreateProposalViewModel = koinViewModel(),
    onBack: () -> Unit,
    onProposalCreated: (proposalUuid: Uuid) -> Unit = {},
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle().value

    EventEffect(
        event = state.submitProposalEvent,
        onConsumed = viewModel::onProposalCreatedEvent
    ) { eventData ->
        if (eventData.error == null) {
            eventData.proposalUuid?.let {
                onProposalCreated(it)
            }
        } else {
            context.showToast(R.string.failed_to_create_proposal)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(CreateProposalScreenIntent.EnterScreen)
    }

    CreateProposalScreen_Ui(
        titleMaxLength = state.titleMaxLength,
        descMaxLength = state.descMaxLength,
        onBack = onBack,
        onSubmit = { title, desc ->
            viewModel.createProposal(
                CreateDraftProposal(title, desc, state.proposalDuration)
            )
        },
        isLoading = state.isLoading,
        onDurationSelected = {
            viewModel.onIntent(CreateProposalScreenIntent.SelectProposalDuration(it))
        },
    )
}

// TODO error
@Composable
private fun CreateProposalScreen_Ui(
    titleMaxLength: Int,
    descMaxLength: Int,
    onDurationSelected: (Duration) -> Unit,
    onBack: () -> Unit = {},
    onSubmit: (
        title: String,
        description: String,
    ) -> Unit = { _, _ -> },
    isLoading: Boolean = false,
) {

    // To hide keyboard
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AppBar(title = stringResource(R.string.new_proposal), onBack = { onBack.invoke() })
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
                    start = 8.dp,
                    end = 8.dp
                )
        ) {
            // Input values
            val titleText = remember { mutableStateOf("") }
            val descText = remember { mutableStateOf("") }

            // TODO remove
            if (BuildConfig.DEBUG) {
                val ctx = LocalContext.current

                // TODO remove
                LaunchedEffect(Unit) {
                    val mock = ProposalConfig.getRandomMockProposalText(ctx)
                    titleText.value = mock.first
                    descText.value = mock.second
                }
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {

                Text(
                    text = stringResource(R.string.choose_proposal_title),
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    value = titleText.value,
                    maxLines = 1,
                    singleLine = true,
                    onValueChange = {
                        if (it.length <= titleMaxLength) {
                            titleText.value = it
                        } else {
                            titleText.value = it.take(titleMaxLength)
                        }
                    },
                    label = { Text(stringResource(R.string.proposal_title)) })

                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End),
                    text = stringResource(id = R.string.field_length_template, titleText.value.length, titleMaxLength)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.choose_proposal_description),
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    value = descText.value,
                    maxLines = 5,
                    onValueChange = {
                        if (it.length <= descMaxLength) {
                            descText.value = it
                        } else {
                            descText.value = it.take(descMaxLength)
                        }
                    },
                    label = { Text(stringResource(R.string.proposal_desc)) },
                )

                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End),
                    text = stringResource(id = R.string.field_length_template, descText.value.length, descMaxLength)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.choose_proposal_duration),
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectorGroup(
                    onSelectedChanged = {
                        onDurationSelected(it.value)
                    },
                    options = listOf(
                        SelectorOption(1.hours, "1 hour"),
                        SelectorOption(24.hours, "24 hours"),
                        SelectorOption(7.days, "7 days"),
                    ),
                )
            }


            Box(Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_proposal),
                    onClick = {
                        onSubmit.invoke(titleText.value, descText.value)
                    },
                    enabled = titleText.value.isNotBlank() && descText.value.isNotBlank()
                )
            }
        }

        if (isLoading) {
            FullscreenProgressBar()
        }
    }
}

@Preview
@Composable
fun CreateProposalScreen_Preview() {
    VoteKtTheme(darkTheme = false) {
        CreateProposalScreen_Ui(
            titleMaxLength = 50,
            descMaxLength = 250,
            onDurationSelected = {}
        )
    }
}
