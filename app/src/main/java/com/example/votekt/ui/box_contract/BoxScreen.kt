package com.example.votekt.ui.box_contract

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BoxScreen(viewModel: BoxViewModel) {

    val balanceState = viewModel.balanceState.collectAsState()
    val infoState = viewModel.netInfoState.collectAsState()
    val boxState = viewModel.boxState.collectAsState().value


    LaunchedEffect(key1 = null, block = {
        viewModel.loadBalance()
        viewModel.loadNetInfo()
        viewModel.readBox()
    })

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimatedContent(targetState = balanceState) {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                text = "${balanceState.value}\nETH",
                textAlign = TextAlign.Center,
                modifier = Modifier.animateContentSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(targetState = balanceState) {
            Text(
                text = "Block: ${infoState.value}",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.animateContentSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(Modifier.padding(16.dp)) {
            BoxCard(boxState = boxState, onUpdateClick = {
                viewModel.updateBox(it)
            })
        }
    }
}