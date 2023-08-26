package com.example.football.presentation.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.viewmodel.StandingsCombinedViewModel
import com.example.football.presentation.viewmodel.StandingsCombinedViewState
import com.example.football.presentation.viewmodel.StandingsViewModel
import com.example.football.presentation.viewmodel.TopGoalScorersViewModel
import com.example.football.presentation.viewmodel.viewstate.LeagueTableViewState
import com.example.football.presentation.viewmodel.viewstate.TopGoalScorersViewState

@Composable
fun StandingsCombinedScreen(
    standingsCombinedViewModel: StandingsCombinedViewModel,
    standingsViewModel: StandingsViewModel,
    topGoalScorerViewModel: TopGoalScorersViewModel
) {
    val nullableStandingsViewState by
    standingsViewModel.standingsViewState.observeAsState()

    val standingsViewState = nullableStandingsViewState ?: LeagueTableViewState.Error

    val nullableViewState by
    topGoalScorerViewModel.topGoalScorerViewState.observeAsState()

    val topGoalViewState = nullableViewState ?: TopGoalScorersViewState.Error

    val nullableCombinedViewState by
    standingsCombinedViewModel.standingsCombinedViewState.observeAsState()

    val standingsCombinedViewState =
        nullableCombinedViewState ?: StandingsCombinedViewState(error = true)

    var seeAllTopGoalScorers by remember {
        mutableStateOf(false)
    }
    var seeWholeLeagueTable by remember {
        mutableStateOf(false)
    }

    val topScorersContent: @Composable () -> Unit = {
        TopScorersTable(
            viewState = topGoalViewState,
            seeAll = seeAllTopGoalScorers
        ) { isSeeAll ->
            seeAllTopGoalScorers = isSeeAll
            standingsCombinedViewModel.onTopGoalScorerSeeAllClicked()
        }
    }
    val standingsContent: @Composable () -> Unit = {
        LeagueTable(
            viewState = standingsViewState,
            isSeeAll = seeWholeLeagueTable
        ) { isSeeAll ->
            seeWholeLeagueTable = isSeeAll
            standingsCombinedViewModel.onStandingsSeeAllClicked()
        }
    }

    Scaffold(modifier = Modifier.padding(16.dp)) { padding ->
        StandingsContent(
            Modifier.padding(paddingValues = padding),
            standingsCombinedViewState,
            standingsContent,
            topScorersContent
        )
    }
}

@Composable
fun StandingsContent(
    modifier: Modifier = Modifier,
    standingsCombinedViewState: StandingsCombinedViewState,
    standingsContent: @Composable () -> Unit,
    topScorersContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when {
            standingsCombinedViewState.combinedViewState -> {
                standingsContent()
                topScorersContent()
            }

            standingsCombinedViewState.topGoalScorerSeeAll -> {
                topScorersContent()
            }

            standingsCombinedViewState.standingsSeeAll -> {
                standingsContent()
            }

            else -> {
                Text(text = "Hello")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun StandingsCombinedScreenPreview() {
    FootballTheme {
        StandingsContent(
            standingsCombinedViewState = StandingsCombinedViewState(combinedViewState = true),
            standingsContent = {
                Content(
                    standingsViewData = PreviewData.standings,
                    seeAllClick = {},
                    isSeeAll = false
                )
            },
            topScorersContent = {
                Content(
                    topGoalScorerViewData = PreviewData.topGoalScorersList,
                    onSeeAllClick = {},
                    seeAll = true
                )
            })
    }
}
