package com.example.votekt.ui.feature.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.ui.components.DotsPagerIndicator
import com.example.votekt.ui.components.PrimaryButton
import com.example.votekt.ui.components.SecondaryButton
import com.example.votekt.ui.components.preview.ScreenPreview
import com.example.votekt.ui.feature.onboarding.model.WelcomePageUi
import com.example.votekt.ui.feature.onboarding.model.WelcomeScreenIntent
import com.example.votekt.ui.feature.onboarding.model.WelcomeScreenNavigationEvent
import com.example.votekt.ui.theme.Dimensions
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel = koinViewModel(),
    onNavigationEvent: (WelcomeScreenNavigationEvent) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    WelcomeScreen_Ui(
        onIntent = {
            viewModel.onIntent(it)
        }
    )

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent
    ) {
        onNavigationEvent(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WelcomeScreen_Ui(
    onIntent: (WelcomeScreenIntent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = Dimensions.screenPaddingHorizontal,
                vertical = Dimensions.screenPaddingVertical,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_to_app),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Spacer(Modifier.weight(1f))

        val pages = WelcomePageUi.getPages(LocalContext.current)
        val pagerState = rememberPagerState(pageCount = { pages.size })

        OnboardingPager(pagerState, pages)

        Spacer(Modifier.weight(1f))

        DotsPagerIndicator(
            totalDots = 3,
            selectedIndex = pagerState.currentPage,
            selectedColor = MaterialTheme.colorScheme.primary,
            unSelectedColor = Color.LightGray,
            dotSize = 12.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.create_account),
            onClick = {
                onIntent(WelcomeScreenIntent.CreateAccountClick)
            }
        )

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.already_have_account),
            onClick = {
                onIntent(WelcomeScreenIntent.AlreadyHaveAccountClick)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPager(
    pagerState: PagerState,
    pages: List<WelcomePageUi>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .wrapContentHeight(),
            state = pagerState,
        ) { pageIndex ->
            val page = pages[pageIndex]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = 80.dp,
                            vertical = 16.dp,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = page.imgRes),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )
                }

                Text(
                    text = page.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 56.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun WelcomeScreen_Preview() {
    ScreenPreview {
        WelcomeScreen_Ui()
    }
}
