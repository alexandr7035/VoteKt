package com.example.votekt.ui.create_proposal

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.example.votekt.domain.votings.CreateProposal
import com.example.votekt.domain.votings.ProposalDuration
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SelectorGroup
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.components.selector_group.SelectorOption
import com.example.votekt.ui.components.snackbar.ResultSnackBar
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.uiError
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.prettifyAddress
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateProposalScreen(
    viewModel: CreateProposalViewModel = koinViewModel(),
    onBack: () -> Unit = {},
    onShowSnackBar: (message: String, snackBarMode: SnackBarMode) -> Unit = { _, _ -> },
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    EventEffect(
        event = state.submitProposalEvent, onConsumed = viewModel::onProposalCreatedEvent
    ) { eventData ->
        if (eventData.isTransactionSubmitted) {
            onShowSnackBar.invoke(
                "Transaction submitted! Wait for the transaction result\n\nHash: ${eventData.transactionHash!!.prettifyAddress()}",
                SnackBarMode.Positive
            )
            
            onBack.invoke()
        } else {
            onShowSnackBar.invoke(
                // TODO ui error model
                "Failed to submit Proposal\n\n${eventData.error?.errorType?.uiError?.title}. ${eventData.error?.errorType?.uiError?.message}",
                SnackBarMode.Negative
            )
        }
    }

    CreateProposalScreen_Ui(
        onBack = onBack,
        onSubmit = { title, desc, duration ->
            viewModel.createProposal(
                CreateProposal(title, desc, duration)
            )
        },
        titleMaxLength = state.titleMaxLength,
        descMaxLength = state.descMaxLength,
        snackarHostState = snackBarHostState,
        isLoading = state.isLoading
    )
}


@SuppressLint("RememberReturnType")
@Composable
private fun CreateProposalScreen_Ui(
    titleMaxLength: Int,
    descMaxLength: Int,
    onBack: () -> Unit = {},
    onSubmit: (title: String, description: String, duration: ProposalDuration) -> Unit = { _, _, _ -> },
    snackarHostState: SnackbarHostState = SnackbarHostState(),
    isLoading: Boolean = false
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
        snackbarHost = {
            SnackbarHost(
                hostState = snackarHostState,
                snackbar = { ResultSnackBar(snackbarData = it) }
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = pv.calculateTopPadding() + 8.dp, bottom = pv.calculateBottomPadding(), start = 8.dp, end = 8.dp
                )
        ) {
            // Input values
            val titleText = remember { mutableStateOf("") }
            val descText = remember { mutableStateOf("") }

            // FIXME better solution
            val selectedDuration = remember {
                mutableStateOf(ProposalDuration.DURATION_24_HOURS)
            }

            if (BuildConfig.DEBUG) {
                val ctx = LocalContext.current

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
                    text = "Choose proposal title", style = MaterialTheme.typography.headlineSmall
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
                        .align(Alignment.End), text = "${titleText.value.length} / $titleMaxLength"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Choose proposal description", style = MaterialTheme.typography.headlineSmall
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
                        .align(Alignment.End), text = "${descText.value.length} / $descMaxLength"
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Choose proposal duration", style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                SelectorGroup(
                    onSelectedChanged = {
                        selectedDuration.value = it.value
                    },
                    options = listOf(
                        SelectorOption(ProposalDuration.DURATION_24_HOURS, ProposalDuration.DURATION_24_HOURS.getDurationInString()),
                        SelectorOption(ProposalDuration.DURATION_7_DAYS, ProposalDuration.DURATION_7_DAYS.getDurationInString()),
                        SelectorOption(ProposalDuration.DURATION_30_DAYS, ProposalDuration.DURATION_30_DAYS.getDurationInString()),
                    ),
                )
            }


            Box(Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.create_proposal),
                    onClick = {
                        onSubmit.invoke(titleText.value, descText.value, selectedDuration.value)
                        Log.d("TEST", "DURATON ${selectedDuration.value}")
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
        CreateProposalScreen_Ui(50, 250)
    }
}
