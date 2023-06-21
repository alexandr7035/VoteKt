package com.example.votekt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.votekt.ui.VotingViewModel
import com.example.votekt.ui.core.AppNavHost
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.voting_details.VotingDetailsScreen
import com.example.votekt.ui.votings_list.ProposalsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = remember { VotingViewModel() }
            val navController = rememberNavController()

            VoteKtTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    BoxScreen(viewModel)
                    AppNavHost(navController = navController)
//                    VotingDetailsScreen(viewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VoteKtTheme {
    }
}