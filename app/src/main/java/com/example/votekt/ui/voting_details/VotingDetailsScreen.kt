package com.example.votekt.ui.voting_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.components.VotersPager
import com.example.votekt.ui.components.VotingBar
import com.example.votekt.ui.components.VotingBarParams

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingDetailsScreen() {
    Row(Modifier.fillMaxSize()) {

        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = "Voting res", style = MaterialTheme.typography.headlineLarge)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        }) { pv ->
            Column(Modifier.padding(start = 16.dp, end = 16.dp, top = pv.calculateTopPadding() + 16.dp)) {
                VotingBar(
                    params = VotingBarParams(
                        100,
                        50,
                        segmentWidth = 20.dp,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                VotersPager()
            }
        }
    }
}