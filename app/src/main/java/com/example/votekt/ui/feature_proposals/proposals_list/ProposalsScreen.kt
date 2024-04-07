package com.example.votekt.ui.feature_proposals.proposals_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.votekt.R
import com.example.votekt.domain.votings.Proposal
import com.example.votekt.ui.components.ErrorFullScreen
import com.example.votekt.ui.components.pager.Page
import com.example.votekt.ui.components.pager.PagerTabRow
import com.example.votekt.ui.components.preview.ProposalListPreviewProvider
import com.example.votekt.ui.components.progress.FullscreenProgressBar
import com.example.votekt.ui.core.AppBar
import com.example.votekt.ui.theme.VoteKtTheme
import com.example.votekt.ui.feature_proposals.components.VotingPostCard
import com.example.votekt.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProposalsScreen(
    onProposalClick: (proposalId: String) -> Unit = {},
    onNewProposalClick: () -> Unit = {},
    viewModel: ProposalsViewModel = koinViewModel()
) {
    Scaffold(
        topBar = {
            AppBar(title = stringResource(R.string.proposals))
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewProposalClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = stringResource(id = R.string.create_proposal)
                )
            }
        }) { pv ->

        val state = viewModel.state.collectAsStateWithLifecycle().value

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
                ProposalsList(
                    proposals = state.proposals,
                    pv = pv,
                    onProposalClick = onProposalClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProposalsList(
    proposals: List<Proposal>,
    pv: PaddingValues,
    onProposalClick: (proposalId: String) -> Unit,
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

        Spacer(Modifier.height(8.dp))

        PagerTabRow(
            tabs = pages,
            pagerState = pagerState
        )

        HorizontalPager(state = pagerState) {
            if (proposalsFiltered.isNotEmpty()) {

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = Dimensions.screenPaddingHorizontal,
                        end = Dimensions.screenPaddingHorizontal,
                        bottom = Dimensions.screenPaddingVertical,
                        top = 8.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.cardListSpacing),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    itemsIndexed(
                        items = proposalsFiltered,
                        key = { _, proposal ->
                            proposal.uuid
                        }
                    ) { _, proposal ->
                        Card(
                            onClick = { onProposalClick(proposal.uuid) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            VotingPostCard(
                                proposal = proposal
                            )
                        }
                    }
                }
            } else {
                NoProposalsStub(modifier = Modifier.fillMaxSize())
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
@Preview()
fun ProposalsScreen_Preview(
    @PreviewParameter(ProposalListPreviewProvider::class) proposals: List<Proposal>
) {
    VoteKtTheme(darkTheme = false) {
        ProposalsList(
            proposals = proposals,
            pv = PaddingValues(),
            onProposalClick = {}
        )
    }
}