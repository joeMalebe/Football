package com.example.football.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.StandingsViewData
import com.example.football.presentation.viewmodel.viewstate.LeagueTableViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun LeagueTable(
    viewState: LeagueTableViewState,
    isSeeAll: Boolean,
    onTeamClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    seeAllClick: (Boolean) -> Unit
) {
    when (viewState) {
        is LeagueTableViewState.StandingsLoaded -> {
            Content(
                standingsViewData = viewState.standings.toImmutableList(),
                modifier = modifier,
                isSeeAll = isSeeAll,
                onTeamClicked = onTeamClicked,
                seeAllClick =
                seeAllClick
            )
        }

        is LeagueTableViewState.Loading -> {
            Loading()
        }

        else -> {
            Text(text = "Hello")
        }
    }
}

@Composable
fun Content(
    standingsViewData: ImmutableList<StandingsViewData>,
    isSeeAll: Boolean,
    onTeamClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    seeAllClick: (Boolean) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        StandingsTable(
            standingsViewData = standingsViewData,
            isSeeAll = isSeeAll,
            onTeamClicked = onTeamClicked,
            seeAllClick = seeAllClick
        )
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1,
    contentAlignment: Alignment = Alignment.Center
) {
    Box(
        modifier = modifier
            .weight(weight)
            .padding(8.dp),
        contentAlignment = contentAlignment
    ) {
        Text(
            text = text,
            style = style
        )
    }
}

@Composable
fun RowScope.ClubTableCell(
    team: StandingsViewData,
    weight: Float,
    modifier: Modifier = Modifier,
    onTeamClicked: (Int) -> Unit
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
            modifier = Modifier.padding(start = 6.dp).clickable { onTeamClicked(team.teamId) }
        )
    }
}

@Composable
fun StandingsTable(
    standingsViewData: ImmutableList<StandingsViewData>,
    isSeeAll: Boolean,
    onTeamClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    seeAllClick: (Boolean) -> Unit
) {
    // weight for team name column and stats column
    val columnClubsWeight = .4f // 60%
    val columnStatsWeight = .125f // 10%
    val columnStandingsHeaderWeight = .8f // 80%
    val columnSeeAllHeaderWeight = .2f // 20%

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
                TableCell(
                    text = stringResource(id = R.string.table_standings),
                    weight = columnStandingsHeaderWeight,
                    style = MaterialTheme.typography.h6,
                    contentAlignment = Alignment.CenterStart
                )
                TableCell(
                    text = stringResource(id = R.string.see_all),
                    weight = columnSeeAllHeaderWeight,
                    style = MaterialTheme.typography.subtitle1,
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.clickable { seeAllClick(!isSeeAll) }
                )
            }
        }
        item {
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = stringResource(id = R.string.clubs),
                    weight = columnClubsWeight,
                    style = MaterialTheme.typography.subtitle1,
                    contentAlignment = Alignment.CenterStart
                )
                TableCell(
                    text = stringResource(id = R.string.wins),
                    weight = columnStatsWeight
                )
                TableCell(
                    text = stringResource(id = R.string.draws),
                    weight = columnStatsWeight
                )
                TableCell(
                    text = stringResource(id = R.string.losses),
                    weight = columnStatsWeight
                )
                TableCell(
                    text = stringResource(id = R.string.points),
                    weight = columnStatsWeight,
                    contentAlignment = Alignment.CenterEnd
                )
            }
        }
        // Here are all the lines of the table.
        items(if (isSeeAll) standingsViewData else standingsViewData.take(5)) { team ->

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                ClubTableCell(
                    team = team,
                    weight = columnClubsWeight,
                    onTeamClicked = onTeamClicked
                )
                TableCell(text = team.wins.toString(), weight = columnStatsWeight)
                TableCell(text = team.draws.toString(), weight = columnStatsWeight)
                TableCell(text = team.losses.toString(), weight = columnStatsWeight)
                TableCell(
                    text = team.points.toString(),
                    weight = columnStatsWeight,
                    contentAlignment = Alignment.CenterEnd
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 720, showBackground = true)
@Composable
fun StandingsContentPreview() {
    Content(standingsViewData = PreviewData.standings, seeAllClick = {
    }, isSeeAll = false, onTeamClicked = {})
}
