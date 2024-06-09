package by.alexandr7035.votekt.ui.feature.proposals.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import by.alexandr7035.votekt.R
import by.alexandr7035.votekt.domain.votings.Proposal
import by.alexandr7035.votekt.ui.components.ErrorFullScreen
import by.alexandr7035.votekt.ui.components.pager.Page
import by.alexandr7035.votekt.ui.components.pager.PagerTabRow
import by.alexandr7035.votekt.ui.components.preview.ProposalListPreviewProvider
import by.alexandr7035.votekt.ui.components.preview.ScreenPreview
import by.alexandr7035.votekt.ui.components.progress.FullscreenProgressBar
import by.alexandr7035.votekt.ui.core.AppBar
import by.alexandr7035.votekt.ui.feature.proposals.components.VotingPostCard
import by.alexandr7035.votekt.ui.theme.Dimensions
import de.palm.composestateevents.NavigationEventEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProposalsScreen(
    onNavigationEvent: (ProposalsScreenNavigationEvent) -> Unit,
    viewModel: ProposalsViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    val nestedScrollConnection = proposalListScrollConnection(viewModel)

    Scaffold(
        topBar = {
            AppBar(title = stringResource(R.string.proposals))
        },
        floatingActionButton = {
            CreateProposalFab(state)
        }
    ) { pv ->

        when {
            state.isLoading -> {
                FullscreenProgressBar(backgroundColor = Color.Transparent)
            }

            state.error != null -> {
                ErrorFullScreen(
                    error = state.error,
                    onRetry = {
                        viewModel.subscribeToProposals()
                    }
                )
            }

            else -> {
                ProposalsScreen_Ui(
                    proposals = state.proposals,
                    pv = pv,
                    nestedScrollConnection = nestedScrollConnection,
                    isTabBarVisible = state.controlsAreVisible
                ) {
                    viewModel.onIntent(it)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProposalsScreenIntent.EnterScreen)
    }

    NavigationEventEffect(
        event = state.navigationEvent,
        onConsumed = viewModel::consumeNavigationEvent
    ) {
        onNavigationEvent(it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProposalsScreen_Ui(
    proposals: List<Proposal>,
    pv: PaddingValues,
    nestedScrollConnection: NestedScrollConnection = rememberNestedScrollInteropConnection(),
    isTabBarVisible: Boolean = true,
    onIntent: (ProposalsScreenIntent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = pv.calculateTopPadding(),
                bottom = pv.calculateBottomPadding(),
            ),
    ) {
        val pages = listOf(
            Page(0, stringResource(R.string.on_chain)),
            Page(1, stringResource(R.string.drafts)),
        )

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f,
            pageCount = { 2 }
        )

        val proposalsFiltered = remember(proposals, pagerState.currentPage) {
            when (pagerState.currentPage) {
                0 -> proposals.filterIsInstance<Proposal.Deployed>()
                1 -> proposals.filterIsInstance<Proposal.Draft>()
                else -> error("Unexpected page index ${pagerState.currentPage}")
            }
        }

        AnimatedVisibility(
            visible = isTabBarVisible,
        ) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                PagerTabRow(
                    tabs = pages,
                    pagerState = pagerState
                )
            }
        }

        HorizontalPager(state = pagerState) { pageIndex ->
            LaunchedEffect(pageIndex) {
                onIntent(ProposalsScreenIntent.ChangeControlsVisibility(true))
            }

            if (proposalsFiltered.isNotEmpty()) {
                ProposalsList(nestedScrollConnection, proposalsFiltered, onIntent)
            } else {
                NoProposalsStub(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
private fun ProposalsList(
    nestedScrollConnection: NestedScrollConnection,
    proposalsFiltered: List<Proposal>,
    onIntent: (ProposalsScreenIntent) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = Dimensions.screenPaddingHorizontal,
            end = Dimensions.screenPaddingHorizontal,
            bottom = Dimensions.screenPaddingVertical,
            top = 8.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(Dimensions.cardListSpacing),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
    ) {
        itemsIndexed(
            items = proposalsFiltered,
            key = { _, proposal ->
                proposal.uuid
            }
        ) { _, proposal ->
            Card(
                onClick = {
                    onIntent(ProposalsScreenIntent.ProposalClick(proposal.uuid))
                },
                elevation = CardDefaults.cardElevation(Dimensions.defaultCardElevation)
            ) {
                VotingPostCard(
                    proposal = proposal,
                    onExplorerClick = { payload, exploreType ->
                        onIntent(
                            ProposalsScreenIntent.ExplorerUrlClick(
                                payload = payload,
                                exploreType = exploreType
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun NoProposalsStub(
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_proposals_yet),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun proposalListScrollConnection(viewModel: ProposalsViewModel) = remember {
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available.y < -1) {
                viewModel.onIntent(ProposalsScreenIntent.ChangeControlsVisibility(false))
            }

            if (available.y > 1) {
                viewModel.onIntent(ProposalsScreenIntent.ChangeControlsVisibility(true))
            }

            return Offset.Zero
        }
    }
}

@Composable
private fun CreateProposalFab(
    state: ProposalsScreenState,
    onIntent: (ProposalsScreenIntent) -> Unit = {},
) {
    AnimatedVisibility(
        visible = state.controlsAreVisible,
        enter = slideInVertically(initialOffsetY = { it * 2 }),
        exit = slideOutVertically(targetOffsetY = { it * 2 }),
    ) {
        FloatingActionButton(
            onClick = {
                onIntent(ProposalsScreenIntent.AddProposalClick)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(id = R.string.create_proposal)
            )
        }
    }
}

@Composable
@Preview
fun ProposalsScreen_Preview(
    @PreviewParameter(ProposalListPreviewProvider::class) proposals: List<Proposal>
) {
    ScreenPreview {
        ProposalsScreen_Ui(
            proposals = proposals,
            pv = PaddingValues()
        )
    }
}
