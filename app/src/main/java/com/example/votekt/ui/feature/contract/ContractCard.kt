package com.example.votekt.ui.feature.contract

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.domain.model.contract.ContractState
import com.example.votekt.domain.model.explorer.ExploreType
import com.example.votekt.ui.components.PercentageIndicator
import com.example.votekt.ui.components.SkeletonShape
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.components.web3.ExplorableText
import com.example.votekt.ui.core.resources.UiText
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.utils.prettifyAddress
import org.kethereum.model.Address

@Composable
fun ContractCard(
    contractState: ContractState?,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit = { _, _ -> },
) {
    Card(
        elevation = CardDefaults.cardElevation(Dimensions.defaultCardElevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(Dimensions.defaultCardCorners)
                )
                .padding(
                    vertical = Dimensions.cardPaddingVertical,
                    horizontal = Dimensions.cardPaddingHorizontal
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            contractState?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    ContractLabel()
                    ContractAddress(contractState.address, onExplorerClick)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        ContactStorageComponent(contractState)
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ContractStatsItem(ContractStats.ON_VOTE, contractState.pendingProposals)
                        ContractStatsItem(ContractStats.SUPPORTED, contractState.supportedProposals)
                        ContractStatsItem(ContractStats.NOT_SUPPORTED, contractState.notSupportedProposals)
                    }
                }
            } ?: run {
                ContractLabel()
                ContractCardSkeleton()
            }
        }
    }
}

@Composable
private fun ContractAddress(
    contractAddress: Address,
    onExplorerClick: (payload: String, exploreType: ExploreType) -> Unit
) {
    ExplorableText(
        text = contractAddress.hex.prettifyAddress(),
        onClick = {
            onExplorerClick(
                contractAddress.hex,
                ExploreType.ADDRESS
            )
        }
    )
}

@Composable
private fun ContactStorageComponent(contractState: ContractState) {
    PercentageIndicator(
        modifier = Modifier.size(100.dp),
        percentage = (contractState.currentProposals / contractState.maxProposals.toFloat()),
        thickness = 12.dp,
        textSize = 20.sp,
    )

    Text(
        text = stringResource(R.string.storage),
        style = TextStyle(
            fontWeight = FontWeight.Medium,
        )
    )
}

@Composable
private fun ContractLabel() {
    Text(
        text = stringResource(R.string.contract),
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
        )
    )
}

@Suppress("MagicNumber")
@Composable
private fun ContractCardSkeleton() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SkeletonShape(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
            )

            Spacer(Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                repeat(3) {
                    SkeletonShape(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp),
                        shape = RoundedCornerShape(4.dp),
                    )
                }
            }
        }
    }
}

enum class ContractStats(
    val title: UiText,
    @DrawableRes val icon: Int,
) {
    ON_VOTE(UiText.StringResource(R.string.on_voting), R.drawable.ic_status_pending),
    SUPPORTED(UiText.StringResource(R.string.supported), R.drawable.ic_status_accepted),
    NOT_SUPPORTED(UiText.StringResource(R.string.not_supported), R.drawable.ic_status_rejected),
}

@Composable
private fun ContractStatsItem(
    contractStats: ContractStats,
    value: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = contractStats.icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = contractStats.title.asString(),
            style = TextStyle(
                fontSize = 16.sp,
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(CONTRACT_METRIC_BG_ALPHA)
                )
                .padding(
                    horizontal = 6.dp,
                    vertical = 4.dp
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = value.toString(),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                )
            )
        }
    }
}

@Preview
@Composable
private fun ContractCard_Preview() {
    ScreenPreview {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContractCard(
                contractState = null,
            )
            ContractCard(
                contractState = ContractState(
                    address = Address("0x12312432443"),
                    owner = Address("0x12312432443"),
                    fullPercentage = 0.85f,
                    pendingProposals = 10,
                    supportedProposals = 20,
                    notSupportedProposals = 50,
                    currentProposals = 10,
                    maxProposals = 12,
                ),
            )
        }
    }
}

private const val CONTRACT_METRIC_BG_ALPHA = 0.75f
