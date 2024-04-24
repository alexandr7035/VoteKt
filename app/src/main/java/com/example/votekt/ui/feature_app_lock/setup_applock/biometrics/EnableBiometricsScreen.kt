package com.example.votekt.ui.feature_app_lock.setup_applock.biometrics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.R
import com.example.votekt.domain.security.model.BiometricsAvailability
import com.example.votekt.ui.app_host.host_utils.LocalScopedSnackbarState
import com.example.votekt.ui.components.DotsProgressIndicator
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.TextBtn
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.core.effects.OnResumeEffect
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.feature_app_lock.ui.BiometricAuthResult
import com.example.votekt.ui.feature_app_lock.ui.BiometricsHelper
import com.example.votekt.ui.feature_app_lock.biometrics.BiometricsPromptUi
import com.example.votekt.ui.utils.findActivity
import com.example.votekt.ui.utils.openBiometricsSettings
import de.palm.composestateevents.EventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun EnableBiometricsScreen(
    viewModel: EnableBiometricsViewModel = koinViewModel(),
    onExit: () -> Unit,
    pinToEncrypt: String,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackBarState = LocalScopedSnackbarState.current

    EnableBiometricsScreen_Ui(
        onIntent = {
            viewModel.emitIntent(it)
        },
        onExit = onExit,
        biometricsPrompt = state.prompt,
        biometricsAvailability = state.biometricsAvailability
    )

    EventEffect(
        event = state.authResultEvent,
        onConsumed = viewModel::consumeAuthEvent,
    ) { result ->
        when (result) {
            is BiometricAuthResult.Success -> {
                onExit()
            }

            is BiometricAuthResult.Failure -> {
                snackBarState.show(result.error, SnackBarMode.Negative)
            }
        }
    }

    OnResumeEffect {
        viewModel.emitIntent(EnableBiometricsIntent.RefreshBiometricsAvailability)
    }

    LaunchedEffect(Unit) {
        viewModel.emitIntent(EnableBiometricsIntent.EnterScreen(pinToEncrypt))
    }
}

@Composable
fun EnableBiometricsScreen_Ui(
    onIntent: (EnableBiometricsIntent) -> Unit = {},
    onExit: () -> Unit = {},
    biometricsPrompt: BiometricsPromptUi,
    biometricsAvailability: BiometricsAvailability = BiometricsAvailability.Checking
) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .height(maxHeight)
                .width(maxWidth)
                .verticalScroll(rememberScrollState())
                .padding(
                    vertical = 40.dp,
                    horizontal = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            Text(
                text = stringResource(R.string.enable_biometrics),
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.biometrics_explanation),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF999999),
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_fingerprint),
                contentDescription = "Biometrics logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(160.dp)
                    .padding(16.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )

            Spacer(Modifier.weight(1f))

            val activity = LocalContext.current.findActivity()

            when (biometricsAvailability) {
                BiometricsAvailability.Checking -> {
                    DotsProgressIndicator(modifier = Modifier.padding(56.dp))
                }

                BiometricsAvailability.Available -> {
                    PrimaryButton(
                        onClick = {
                            activity?.let {
                                val data = BiometricsHelper.buildBiometricsPrompt(
                                    activity,
                                    onSuccess = {
                                        onIntent(
                                            EnableBiometricsIntent.ConsumeAuthResult(
                                                BiometricAuthResult.Success(it)
                                            )
                                        )
                                    },
                                    onError = {
                                        onIntent(
                                            EnableBiometricsIntent.ConsumeAuthResult(
                                                BiometricAuthResult.Failure(it)
                                            )
                                        )
                                    },
                                    prompt = biometricsPrompt
                                )

                                onIntent(EnableBiometricsIntent.AuthenticateWithBiometrics(data))
                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.setup_biometrics)
                    )
                }

                BiometricsAvailability.NotAvailable -> {
                    Text(
                        text = stringResource(R.string.error_biometrics_not_available),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFF9800),
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    PrimaryButton(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.setup_biometrics)
                    )
                }

                BiometricsAvailability.NotEnabled -> {
                    Text(
                        text = stringResource(R.string.biometrics_enable_explanation),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = Color(0xFFFF9800),
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    PrimaryButton(
                        onClick = {
                            activity?.openBiometricsSettings()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.go_to_settings)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextBtn(
                onClick = {
                    onExit.invoke()
                },
                modifier = Modifier.wrapContentSize(),
                text = stringResource(id = R.string.skip_this_step)
            )
        }
    }
}

@Composable
@Preview
fun EnableBiometricsScreen_Preview() {
    ScreenPreview {
        EnableBiometricsScreen_Ui(
            biometricsPrompt = BiometricsPromptUi(
                title = UiText.StringResource(R.string.setup_biometrics),
                cancelBtnText = UiText.StringResource(R.string.cancel)
            ),
            biometricsAvailability = BiometricsAvailability.Available
        )
    }
}

@Composable
@Preview
fun EnableBiometricsScreen_Not_Enrolled_Preview() {
    ScreenPreview {
        EnableBiometricsScreen_Ui(
            biometricsPrompt = BiometricsPromptUi(
                title = UiText.StringResource(R.string.setup_biometrics),
                cancelBtnText = UiText.StringResource(R.string.cancel)
            ),
            biometricsAvailability = BiometricsAvailability.NotEnabled
        )
    }
}


@Composable
@Preview
fun EnableBiometricsScreen_Not_Available_Preview() {
    ScreenPreview {
        EnableBiometricsScreen_Ui(
            biometricsPrompt = BiometricsPromptUi(
                title = UiText.StringResource(R.string.setup_biometrics),
                cancelBtnText = UiText.StringResource(R.string.cancel)
            ),
            biometricsAvailability = BiometricsAvailability.NotAvailable
        )
    }
}

@Composable
@Preview
fun EnableBiometricsScreen_Loading_Preview() {
    ScreenPreview {
        EnableBiometricsScreen_Ui(
            biometricsPrompt = BiometricsPromptUi(
                title = UiText.StringResource(R.string.setup_biometrics),
                cancelBtnText = UiText.StringResource(R.string.cancel)
            ),
            biometricsAvailability = BiometricsAvailability.Checking
        )
    }
}