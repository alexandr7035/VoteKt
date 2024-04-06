package com.example.votekt.ui.components.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.votekt.ui.theme.Dimensions
import com.example.votekt.ui.theme.VoteKtTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> PagerTabRow(
    tabs: List<Page<T>>,
    pagerState: PagerState
) {
    require(pagerState.pageCount == tabs.size) {
        "Pages count should match tabs count"
    }

    val scope = rememberCoroutineScope()

    SecondaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        divider = @Composable {},
        modifier = Modifier.fillMaxWidth(),
        indicator = { },
    ) {

        tabs.forEachIndexed { tabIndex, tab ->
            PagerTab(
                isSelected = pagerState.currentPage == tabIndex,
                text = tab.title,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(tabIndex)
                    }
                })
        }
    }

}


@Composable
fun PagerTab(
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit = {}
) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimensions.tabCorners))
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                }
            ),
    ) {

        Box(
            modifier = Modifier.padding(vertical = Dimensions.tabPaddingVertical),
            contentAlignment = Alignment.Center
        ) {

            if (isSelected) {
                Text(
                    text = text, style = TextStyle(
                        fontSize = Dimensions.tabTextSize,
                        lineHeight = Dimensions.tabTextLineHeight,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            } else {
                Text(
                    text = text, style = TextStyle(
                        fontSize = Dimensions.tabTextSize,
                        lineHeight = Dimensions.tabTextLineHeight,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
private fun TabRow_Preview() {
    VoteKtTheme() {
        Surface(color = MaterialTheme.colorScheme.background) {
            PagerTabRow(
                pagerState = rememberPagerState() {
                    2
                },
                tabs = listOf(
                    Page(0, "Sample1"),
                    Page(1, "Sample2"),
                )
            )
        }
    }
}


