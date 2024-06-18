package by.alexandr7035.votekt.ui.feature.wallet.share

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.ui.components.DotsProgressIndicator
import by.alexandr7035.votekt.ui.components.SecondaryButton
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.qr.QrCodeCard
import by.alexandr7035.votekt.ui.components.web3.ExplorableText
import by.alexandr7035.votekt.ui.core.effects.OnResumeEffect
import by.alexandr7035.votekt.ui.feature.wallet.share.model.SelfWalletDialogIntent
import by.alexandr7035.votekt.ui.feature.wallet.share.model.SelfWalletDialogState
import by.alexandr7035.votekt.ui.utils.copyToClipboard
import by.alexandr7035.votekt.ui.utils.prettify
import by.alexandr7035.votekt.ui.utils.showToast
import org.kethereum.model.Address
import org.koin.androidx.compose.koinViewModel

private val QR_SIZE_FIXED = 160.dp
private const val ADDRESS_PRETTIFIED_LENGTH = 10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfWalletQrDialogScreen(
    viewModel: SelfWalletQrDialogViewModel = koinViewModel(),
    onDismiss: () -> Unit = {},
    onAddressClick: () -> Unit,
) {
    val dialogState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val state = viewModel.state.collectAsStateWithLifecycle().value

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = dialogState,
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        SelfWalletQrDialogScreen_Ui(
            state = state,
            onAddressClick = onAddressClick
        )
    }

    OnResumeEffect {
        viewModel.emitIntent(SelfWalletDialogIntent.EnterScreen)
    }
}

@Composable
fun SelfWalletQrDialogScreen_Ui(
    state: SelfWalletDialogState,
    onAddressClick: () -> Unit = {},
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        state.address?.let {
            ExplorableText(
                text = it.prettify(
                    prefixLength = ADDRESS_PRETTIFIED_LENGTH,
                    suffixLength = ADDRESS_PRETTIFIED_LENGTH,
                ),
                onClick = {
                    onAddressClick()
                },
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.padding(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DotsProgressIndicator()
                }
            }

            state.address != null -> {
                ScreenContent(state.address)
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error.asString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ScreenContent(address: Address) {
    val context = LocalContext.current

    QrCodeCard(
        modifier = Modifier.width(QR_SIZE_FIXED),
        qr = address.hex
    )

    Spacer(Modifier.height(16.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SecondaryButton(
            text = stringResource(id = R.string.copy),
            onClick = {
                context.copyToClipboard(R.string.address_copied, address.hex)
                context.showToast(R.string.address_copied)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        SecondaryButton(
            text = stringResource(id = R.string.share),
            onClick = {
                shareAddressWithIntent(context, address.hex)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        )
    }
}

private fun shareAddressWithIntent(context: Context, address: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.my_evm_address_template, address))
    }

    val shareIntent = Intent.createChooser(intent, context.getString(R.string.share_address_via))
    startActivity(context, shareIntent, bundleOf())
}

@Preview
@Composable
fun SelfWalletQrDialogScreen_Preview() {
    ScreenPreview {
        SelfWalletQrDialogScreen_Ui(
            state = SelfWalletDialogState(
                isLoading = false,
                address = Address("0x324324")
            ),
        )
    }
}
