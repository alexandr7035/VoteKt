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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.R
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.utils.showToast

@Composable
fun WalletScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Header()
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(32.dp)
            .fillMaxWidth()
            .wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Balance()
        Actions()
    }
}

@Composable
private fun Balance() {
    Text(
        style = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        ),
        text = "200 ETH"
    )
}

@Composable
private fun Actions() {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val context = LocalContext.current

        ActionBtn(icon = R.drawable.ic_send) {
            context.showToast("Send")
        }

        ActionBtn(icon = R.drawable.ic_receive) {
            context.showToast("Receive")
        }

        ActionBtn(icon = R.drawable.ic_vote_outlined) {
            context.showToast("Vote")
        }
    }
}

@Composable
private fun ActionBtn(
    @DrawableRes icon: Int,
    size: Dp = 72.dp,
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
            WalletScreen()
        }
    }
}