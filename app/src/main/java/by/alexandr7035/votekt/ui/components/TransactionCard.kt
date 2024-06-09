package by.alexandr7035.votekt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.alexandr7035.ethereum.model.WEI
import by.alexandr7035.ethereum.model.Wei
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.core.extensions.getFormattedDate
import by.alexandr7035.votekt.domain.model.explorer.ExploreType
import by.alexandr7035.votekt.domain.transactions.TransactionDomain
import by.alexandr7035.votekt.domain.transactions.TransactionType
import by.alexandr7035.votekt.ui.components.preview.TransactionPreviewProvider
import by.alexandr7035.votekt.ui.components.web3.ExplorableText
import by.alexandr7035.votekt.ui.core.resources.UiText
import by.alexandr7035.votekt.ui.feature.transactions.history.model.getTransactionStatusUi
import by.alexandr7035.votekt.ui.theme.Dimensions
import by.alexandr7035.votekt.ui.theme.VoteKtTheme
import by.alexandr7035.votekt.ui.utils.BalanceFormatter
import by.alexandr7035.votekt.ui.utils.prettifyAddress

private const val TRANSACTION_DATE_FORMAT = "dd MMM yyyy HH:mm:ss"

@Composable
fun TransactionCard(
    transaction: TransactionDomain,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit
) {
    TransactionCardUi(
        transaction = transaction,
        onExplorerClick = onExplorerClick,
    )
}

@Composable
private fun TransactionCardUi(
    transaction: TransactionDomain,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(
                vertical = Dimensions.cardPaddingVertical,
                horizontal = Dimensions.cardPaddingHorizontal,
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val statusUi = remember(transaction.status) {
                    transaction.status.getTransactionStatusUi()
                }

                Image(
                    painter = painterResource(id = statusUi.iconRes),
                    contentDescription = "Transaction status ${statusUi.status}",
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = transaction.type.getTransactionUiType().asString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )

                ExplorableText(
                    text = transaction.hash.prettifyAddress(),
                    onClick = {
                        onExplorerClick(transaction.hash, ExploreType.TRANSACTION)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            ValueComponent(transaction)

            transaction.gasFee?.let {
                GasFeeComponent(it)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = transaction.dateSent.getFormattedDate(TRANSACTION_DATE_FORMAT),
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                ),
            )
        }
    }
}

@Composable
private fun ValueComponent(transaction: TransactionDomain) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "- ${
                BalanceFormatter.formatAmount(
                    amount = transaction.value?.toEther() ?: 0.WEI.toEther(),
                )
            }",
            fontSize = 28.sp,
        )

        Image(
            modifier = Modifier.size(28.dp),
            painter = painterResource(id = R.drawable.ic_ethereum),
            contentDescription = stringResource(R.string.eth)
        )
    }
}

@Composable
private fun GasFeeComponent(it: Wei) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(
            R.string.gas_fee_template,
            BalanceFormatter.formatAmountWithSymbol(
                amount = it.toEther(),
                symbol = stringResource(R.string.ticker_eth)
            )
        ),
        style = TextStyle(
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun TransactionType.getTransactionUiType(): UiText {
    return when (this) {
        TransactionType.CREATE_PROPOSAL -> UiText.StringResource(R.string.deploy_proposal)
        TransactionType.VOTE -> UiText.StringResource(R.string.make_vote)
        TransactionType.PAYMENT -> UiText.StringResource(R.string.send_eth)
    }
}

@Preview
@Composable
fun TransactionCard_Preview(
    @PreviewParameter(TransactionPreviewProvider::class) transaction: TransactionDomain
) {
    VoteKtTheme {
        TransactionCardUi(
            transaction = transaction,
            onExplorerClick = { _, _ -> }
        )
    }
}
