package com.example.votekt.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.votekt.ui.theme.VoteKtTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VotersPager(modifier: Modifier = Modifier) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .then(modifier)
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            5
        }

        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
        ) { page ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // FIXME
                (0..4).forEach {
                    VotedAddress(address = "0x242524524524545", votedFor = false)
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
        VotersPager()
    }
}