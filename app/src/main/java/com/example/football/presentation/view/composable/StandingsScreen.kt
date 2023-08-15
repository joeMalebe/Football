package com.example.football.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.StandingsViewData
import com.example.football.presentation.viewmodel.StandingsViewModel
import com.example.football.presentation.viewmodel.viewstate.StandingsViewState

@Composable
fun StandingsScreen(viewModel: StandingsViewModel) {
    val nullableViewState by
        viewModel.standingsViewState.observeAsState()

    val viewState = nullableViewState ?: return
    Scaffold { paddingValues ->
        when (viewState) {
            is StandingsViewState.StandingsLoaded -> {
                Content(
                    standingsViewData = viewState.standings,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is StandingsViewState.Loading -> {
                Loading()
            }
            else -> {
                Text(text = "Hello")
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier, standingsViewData: List<StandingsViewData>) {
    StandingsTable(standingsViewData = standingsViewData, modifier = modifier.padding(16.dp))
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .weight(weight)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun RowScope.TableHeaderCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .weight(weight)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun RowScope.TableClubHeaderCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .weight(weight)
            .padding(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun RowScope.ClubTableCell(
    team: StandingsViewData,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .weight(weight)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 6.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.Black)
        )
        val teamName =
            team.teamName
                ?: stringResource(id = R.string.unknown)
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(team.logo)
                .crossfade(true)
                .build(),
            contentDescription = "$teamName club logo",
            placeholder = painterResource(
                id = R.drawable.team_logo
            ),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = teamName,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun StandingsTable(standingsViewData: List<StandingsViewData>, modifier: Modifier = Modifier) {
    // weight for team name column and stats column
    val columnClubsWeight = .5f // 60%
    val columnStatsWeight = .125f // 10%

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        // Here is the header
        item {
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableClubHeaderCell(
                    text = stringResource(id = R.string.clubs),
                    weight = columnClubsWeight
                )
                TableHeaderCell(
                    text = stringResource(id = R.string.wins),
                    weight = columnStatsWeight
                )
                TableHeaderCell(
                    text = stringResource(id = R.string.draws),
                    weight = columnStatsWeight
                )
                TableHeaderCell(
                    text = stringResource(id = R.string.losses),
                    weight = columnStatsWeight
                )
                TableHeaderCell(
                    text = stringResource(id = R.string.points),
                    weight = columnStatsWeight
                )
            }
        }
        // Here are all the lines of the table.
        items(count = standingsViewData.size) { index ->
            val team = standingsViewData[index]
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ClubTableCell(team = team, weight = columnClubsWeight)
                TableCell(text = team.wins.toString(), weight = columnStatsWeight)
                TableCell(text = team.draws.toString(), weight = columnStatsWeight)
                TableCell(text = team.losses.toString(), weight = columnStatsWeight)
                TableCell(text = team.points.toString(), weight = columnStatsWeight)
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 720, showBackground = true)
@Composable
fun StandingsContentPreview() {
    Content(standingsViewData = PreviewData.standings)
}
