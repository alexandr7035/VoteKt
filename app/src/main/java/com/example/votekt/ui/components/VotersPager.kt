package com.example.votekt.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VotersPager(
    votedAddresses: List<VoterAddress>,
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .then(modifier)
    ) {
        val splitted = votedAddresses.chunked(5)

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            splitted.size
        }

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) { page ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                val addresses = splitted[pagerState.currentPage]
                addresses.forEach {
                    VotedAddress(address = it.address, votedFor = it.votedFor)
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            DotsIndicator(
                totalDots = pagerState.pageCount,
                selectedIndex = pagerState.currentPage,
                selectedColor = MaterialTheme.colorScheme.primary,
                unSelectedColor = MaterialTheme.colorScheme.tertiary,
                size = 16.dp,
                dotSpace = 4.dp,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

    }

}

@Preview(widthDp = 360)
@Composable
fun VotersPager_Preview() {
    VoteKtTheme(darkTheme = false) {
        VotersPager(
            listOf(
                VoterAddress("0x343434", false),
                VoterAddress("0x343434", false)
            )
        )
    }
}

data class VoterAddress(
    val address: String,
    val votedFor: Boolean
)