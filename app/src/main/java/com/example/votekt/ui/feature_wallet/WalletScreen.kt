package com.example.votekt.ui.feature_wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.ui.theme.VoteKtTheme

@Composable
fun WalletScreen() {
    Column(Modifier.fillMaxSize()) {
        Header()
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(20.dp)
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Balance()
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

@Preview
@Composable
private fun WalletScreen_Preview() {
    VoteKtTheme() {
        Surface(color =MaterialTheme.colorScheme.background) {
            WalletScreen()
        }
    }
}