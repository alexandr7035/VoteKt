package com.example.votekt.ui.feature_confirm_transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.alexandr7035.ethereum.model.WEI
import com.example.votekt.R
import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.isContractInteraction
import com.example.votekt.ui.components.DotsProgressIndicator
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.preview.TransactionReviewPreviewProvider
import com.example.votekt.ui.components.web3.ExplorableText
import com.example.votekt.ui.feature_app_lock.ui.BiometricsHelper
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.ui.utils.findActivity
import com.example.votekt.ui.utils.prettifyAddress
import de.palm.composestateevents.EventEffect
import org.kethereum.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTransactionDialog(
    onIntent: (ReviewTransactionIntent) -> Unit,
    state: ReviewTransactionDataUi,
    onBiometricAuthEventConsumed: () -> Unit,
) {
    val context = LocalContext.current

    val dialogState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            onIntent(ReviewTransactionIntent.DismissDialog)
        }, sheetState = dialogState, containerColor = MaterialTheme.colorScheme.background
    ) {
        ConfirmTransactionDialog_Ui(
            state = state,
            onConfirmTransaction = {
                onIntent(ReviewTransactionIntent.SubmitTransactionClick)
            },
            onExplorerClick = { payload, exploreType ->
                onIntent(ReviewTransactionIntent.ExplorerUrlClick(
                    payload = payload,
                    exploreType = exploreType
                ))
            }
        )
    }

    EventEffect(
        event = state.showBiometricConfirmationEvent,
        onConsumed = {
            onBiometricAuthEventConsumed()
        }) {
        val activity = context.findActivity()
        activity?.let {
            val data = BiometricsHelper.buildBiometricsPrompt(
                activity = activity,
                prompt = state.biometricsPromptState,
                onError = {
                    onIntent(ReviewTransactionIntent.BiometricAuthUnlock(isSuccessful = false))
                },
                onSuccess = {
                    onIntent(ReviewTransactionIntent.BiometricAuthUnlock(isSuccessful = true))
                }
            )

            data.first.authenticate(data.second)
        }
    }
}

@Composable
fun ConfirmTransactionDialog_Ui(
    state: ReviewTransactionDataUi,
    onConfirmTransaction: () -> Unit,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.confirm_transaction), style = MaterialTheme.typography.titleLarge
        )

        AmountComponent(
            txReview = state,
            onExplorerClick = onExplorerClick
        )

        TransactionFeeComponent(txReview = state)

        PrimaryButton(
            text = stringResource(id = R.string.confirm_transaction),
            onClick = onConfirmTransaction,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isConfirmBtnEnabled,
        )
    }
}

@Composable
private fun TransactionFeeComponent(txReview: ReviewTransactionDataUi) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.estimated_gas), style = MaterialTheme.typography.titleSmall
            )

            txReview.totalEstimatedFee?.let { weiFee ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = BalanceFormatter.formatAmount(
                            amount = weiFee.toEther(),
                        ),
                        style = MaterialTheme.typography.titleSmall,
                    )

                    Image(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.ic_ethereum),
                        contentDescription = stringResource(R.string.eth)
                    )
                }
            } ?: run {
                EstimationProgressIndicator(txReview)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.including_miner_tip), style = MaterialTheme.typography.titleSmall
            )

            txReview.minerTipFee?.let { minerTipFee ->
                Text(
                    text = BalanceFormatter.formatAmountWithSymbol(
                        amount = minerTipFee.toGWei(), symbol = stringResource(R.string.gwei)
                    ), style = MaterialTheme.typography.titleSmall
                )
            } ?: run {
                EstimationProgressIndicator(txReview)
            }
        }

        txReview.error?.let {
            Spacer(Modifier.height(12.dp))
            Text(
                text = it.asString(), style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                )
            )
        }
    }
}

@Composable
private fun AmountComponent(
    txReview: ReviewTransactionDataUi,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = getTransactionTypeText(txReview))

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = BalanceFormatter.formatAmount(
                    amount = txReview.value?.toEther() ?: 0.WEI.toEther(),
                ),
                fontSize = 28.sp,
            )

            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_ethereum),
                contentDescription = stringResource(R.string.eth)
            )

            Spacer(Modifier.weight(1f))

            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_arrow_doubled),
                contentDescription = stringResource(id = R.string.send_to),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Spacer(Modifier.width(20.dp))

            RecipientBubble(
                recipient = txReview.recipient,
                isContract = txReview.transactionType.isContractInteraction(),
                onAddressClick = {
                    onExplorerClick(txReview.recipient.hex, ExploreType.ADDRESS)
                }
            )
        }
    }
}

@Composable
private fun getTransactionTypeText(txReview: ReviewTransactionDataUi) = when (txReview.transactionType) {
    TransactionType.CREATE_PROPOSAL -> stringResource(id = R.string.create_proposal)
    TransactionType.VOTE -> stringResource(id = R.string.vote)
    TransactionType.PAYMENT -> stringResource(R.string.send_amount)
}

@Composable
fun RecipientBubble(
    recipient: Address,
    isContract: Boolean,
    onAddressClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(0.7f), shape = RoundedCornerShape(16.dp)
            )
            .padding(
                horizontal = 12.dp, vertical = 8.dp
            ), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ExplorableText(
            text = recipient.hex.prettifyAddress(),
            onClick = onAddressClick,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp,
        )

        if (isContract) {
            Text(
                text = "(${stringResource(id = R.string.contract)})",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                ),
            )
        }
    }

}

@Composable
private fun EstimationProgressIndicator(txReview: ReviewTransactionDataUi) {
    if (txReview.error == null) {
        DotsProgressIndicator(
            circleSize = 12.dp, spaceBetween = 4.dp
        )
    } else {
        Text(
            text = stringResource(R.string.metric_na),
            style = TextStyle(
                fontSize = 16.sp, fontWeight = FontWeight.SemiBold
            ),
        )
    }
}

@Preview
@Composable
fun ConfirmTransactionDialog_Preview(
    @PreviewParameter(TransactionReviewPreviewProvider::class) txReview: ReviewTransactionDataUi
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            ConfirmTransactionDialog_Ui(
                state = txReview,
                onConfirmTransaction = {},
                onExplorerClick = { _, _ -> },
            )
        }
    }
}
