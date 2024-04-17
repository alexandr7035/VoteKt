package com.example.votekt.ui.feature_wallet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.ethereum.model.Address
import com.example.votekt.R
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.feature_wallet.model.WalletScreenIntent
import com.example.votekt.ui.feature_wallet.model.WalletScreenNavigationEvent
import com.example.votekt.ui.feature_wallet.model.WalletScreenState
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.copyToClipboard
import com.example.votekt.ui.utils.prettify
import com.example.votekt.ui.utils.showToast
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun WalletScreen(
    viewModel: WalletViewModel = koinViewModel(),
    onNavigationEvent: (WalletScreenNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onWalletIntent(WalletScreenIntent.LoadData)
    }

    WalletScreen_Ui(
        state = state,
        onIntent = {
            viewModel.onWalletIntent(it)
        }
    )

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent,
        action = {
            onNavigationEvent(it)
        }
    )
}

@Composable
private fun WalletScreen_Ui(
    state: WalletScreenState,
    onIntent: (WalletScreenIntent) -> Unit = {}
) {
    when {
        state.error != null -> ErrorFullScreen(
            error = state.error,
            onRetry = {
                onIntent(WalletScreenIntent.LoadData)
            }
        )

        else -> {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Header(
                    onIntent = onIntent,
                    balance = state.balanceFormatted,
                    isBalanceLoading = state.isBalanceLoading,
                    address = state.address,
                )

                if (state.noConnection) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFD86C6C)
                            )
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.error_node_no_connection),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )

                        // TODO retry
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    address: Address,
    balance: String?,
    isBalanceLoading: Boolean,
    onIntent: (WalletScreenIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                top = 12.dp,
                start = Dimensions.screenPaddingHorizontal,
                end = Dimensions.screenPaddingVertical,
                bottom = 32.dp
            )
            .fillMaxWidth()
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CenterAlignedTopAppBar(
            title = {
                AddressComponent(address)
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(
                    onClick = {
                        onIntent(WalletScreenIntent.LogOut)
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = stringResource(R.string.log_out),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )


        Balance(
            balance = balance,
            isBalanceLoading = isBalanceLoading
        )
        Actions(
            onWalletAction = onIntent
        )
    }
}

@Composable
private fun AddressComponent(address: Address) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(16.dp), color = Color(0x40FFFFFF)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                context.copyToClipboard("Address", address.value)
                context.showToast("Address copied")
            }
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp
            )
    ) {
        Text(
            text = address.prettify(),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
private fun Balance(
    balance: String?,
    isBalanceLoading: Boolean,
) {
    when {
        isBalanceLoading -> {
            // TODO loading
            Text(
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                text = "..."
            )
        }

        balance != null -> {
            Text(
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                text = balance
            )
        }

        else -> error("Balance is null")
    }
}

@Composable
private fun Actions(
    onWalletAction: (WalletScreenIntent.WalletAction) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val context = LocalContext.current

        ActionBtn(icon = R.drawable.ic_send) {
            onWalletAction(WalletScreenIntent.WalletAction.Send)
        }

        ActionBtn(icon = R.drawable.ic_receive) {
            onWalletAction(WalletScreenIntent.WalletAction.Receive)
        }

        ActionBtn(icon = R.drawable.ic_vote_outlined) {
            onWalletAction(WalletScreenIntent.WalletAction.Vote)
        }
    }
}

@Composable
private fun ActionBtn(
    @DrawableRes icon: Int,
    size: Dp = 48.dp,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = rememberRipple(radius = size / 2, bounded = false),
                interactionSource = interactionSource
            )
            .background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
            )
            .size(size)
            .padding(size / 4)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = icon),
            // TODO
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
private fun WalletScreen_Preview() {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            WalletScreen_Ui(
                state = WalletScreenState(
                    noConnection = true
                ),
            )
        }
    }
}