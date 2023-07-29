package com.example.votekt.ui.voting_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.ui.VotingViewModel
import com.example.votekt.ui.components.SkeletonShape
import com.example.votekt.ui.components.VotersPager
import com.example.votekt.ui.components.VotingBar
import com.example.votekt.ui.components.VotingBarParams
import com.example.votekt.ui.core.AppBar
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingDetailsScreen(
    proposalId: String,
    onBack: () -> Unit = {},
    viewModel: VotingViewModel = koinViewModel(),
) {
    val addressesState = viewModel.getVotedAddressesObservable().collectAsStateWithLifecycle().value

    Row(Modifier.fillMaxSize()) {

        Scaffold(
            topBar = {
                AppBar(title = "Proposal #${proposalId}") {
                    onBack.invoke()
                }
            }) { pv ->

            LaunchedEffect(Unit) {
                viewModel.loadVotedAddresses()
            }

            Column(Modifier.padding(start = 16.dp, end = 16.dp, top = pv.calculateTopPadding() + 16.dp)) {
                VotingBar(
                    params = VotingBarParams(
                        100,
                        50,
                        segmentWidth = 20.dp,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    addressesState.shouldShowData() -> {
                        VotersPager(votedAddresses = addressesState.data!!)
                    }

                    addressesState.shouldShowFullLoading() || addressesState.shouldShowPartialLoading() -> {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..5).forEach {
                                SkeletonShape(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                )
                            }
                        }
                    }

                    // TODO error
                    addressesState.shouldShowFullError() || addressesState.shouldShowPartialError() -> {
//                        Text(fontSize = 36.sp, text = addressesState.error?.message!!)
                    }
                }
            }
        }
    }
}