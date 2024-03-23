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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.ui.components.DotsProgressIndicator
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.preview.TransactionReviewPreviewProvider
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.ui.utils.prettifyAddress
import org.kethereum.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTransactionDialog(
    onIntent: (ReviewTransactionIntent) -> Unit,
    state: ReviewTransactionData,
) {
    val dialogState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = {
            onIntent(ReviewTransactionIntent.DismissDialog)
        },
        sheetState = dialogState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        ConfirmTransactionDialog_Ui(
            state = state,
            onConfirmTransaction = {
                onIntent(ReviewTransactionIntent.SubmitTransaction)
            }
        )
    }
}

@Composable
fun ConfirmTransactionDialog_Ui(
    state: ReviewTransactionData,
    onConfirmTransaction: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.confirm_transaction),
            style = MaterialTheme.typography.titleLarge
        )

        when (state) {
            is ReviewTransactionData.ContractInteraction -> ContractInputComponent(state)
            is ReviewTransactionData.SendAmount -> SendAmountComponent(state)
        }

        TransactionFeeComponent(txReview = state)

        PrimaryButton(
            text = stringResource(id = R.string.confirm_transaction),
            onClick = onConfirmTransaction,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canSendTransaction()
        )
    }
}

@Composable
private fun TransactionFeeComponent(txReview: ReviewTransactionData) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.estimated_gas),
                style = MaterialTheme.typography.titleSmall
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
                DotsProgressIndicator()
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.including_miner_tip),
                style = MaterialTheme.typography.titleSmall
            )

            txReview.minerTipFee?.let { minerTipFee ->
                Text(
                    text = BalanceFormatter.formatAmountWithSymbol(
                        amount = minerTipFee.toGWei(),
                        symbol = stringResource(R.string.gwei)
                    ),
                    style = MaterialTheme.typography.titleSmall
                )
            } ?: run {
                DotsProgressIndicator()
            }
        }

        if (txReview.isBalanceSufficient == false) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.insufficient_balance),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                )
            )
        }
    }
}


@Composable
private fun ContractInputComponent(txReview: ReviewTransactionData.ContractInteraction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = when (txReview.transactionType) {
                    TransactionType.CREATE_PROPOSAL -> stringResource(id = R.string.create_proposal)
                    TransactionType.VOTE -> stringResource(id = R.string.vote)
                    else -> stringResource(R.string.unknown_interaction)
                }
            )

            AddressComponent(recipient = txReview.recipient)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ABI",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )

            Text(
                text = txReview.contractInput,
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SendAmountComponent(txReview: ReviewTransactionData.SendAmount) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.send_to))
            AddressComponent(recipient = txReview.recipient)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "-${
                    BalanceFormatter.formatAmount(
                        amount = txReview.value.toEther(),
                    )
                }",
                fontSize = 32.sp,
            )

            Image(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_ethereum),
                contentDescription = stringResource(R.string.eth)
            )
        }

    }
}

@Composable
private fun AddressComponent(recipient: Address) {
    Text(
        text = recipient.hex.prettifyAddress(),
        style = TextStyle(
            textDecoration = TextDecoration.Underline
        )
    )
}


@Preview
@Composable
fun ConfirmTransactionDialog_Preview(
    @PreviewParameter(TransactionReviewPreviewProvider::class) txReview: ReviewTransactionData
) {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            ConfirmTransactionDialog_Ui(
                state = txReview,
                onConfirmTransaction = {}
            )
        }
    }
}