package com.example.votekt.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.utils.BalanceFormatter
import com.example.votekt.core.extensions.getFormattedDate
import com.example.votekt.domain.model.blockchain_explorer.ExploreType
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.ui.components.preview.TransactionPreviewProvider
import com.example.votekt.ui.components.web3.ExplorableText
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.getTransactionStatusUi
import com.example.votekt.ui.utils.prettifyAddress
import com.example.votekt.ui.utils.showToast

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
            Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
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

            transaction.gasFee?.let {
                Text(
                    text = "- ${
                        BalanceFormatter.formatAmountWithSymbol(
                            amount = it.toEther(),
                            symbol = stringResource(R.string.ticker_eth)
                        )
                    }",
                    style = TextStyle(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = transaction.dateSent.getFormattedDate("dd MMM yyyy HH:mm:ss"),
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
