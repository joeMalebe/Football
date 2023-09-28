package com.example.football.presentation.view.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.FixtureResultViewData
import com.example.football.domain.FixturesViewData
import com.example.football.domain.TeamFixtureViewData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FixtureScreen(
    fixtureViewData: ImmutableList<FixturesViewData>,
    fixtureResultViewData: ImmutableList<FixtureResultViewData>,
    onFixtureClicked: (fixtureId: String) -> Unit,
) {
    Content(fixtureViewData, fixtureResultViewData, onFixtureClicked)
}

@Composable
private fun Content(
    fixtureViewData: ImmutableList<FixturesViewData>,
    fixtureResultViewData: ImmutableList<FixtureResultViewData>,
    onFixtureClicked: (fixtureId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column() {
        MatchResults(
            fixtureResultViewData,
            Modifier
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp),
        onFixtureClicked = onFixtureClicked)
        UpcomingFixtures(fixtureViewData, modifier.padding(16.dp), onFixtureClicked = onFixtureClicked)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MatchResults(
    fixtureResultViewData: ImmutableList<FixtureResultViewData>,
    modifier: Modifier = Modifier,
    logoSize: Dp = 72.dp,
    onFixtureClicked: (fixtureId: String) -> Unit
) {
    val state = rememberPagerState(initialPageOffsetFraction = 0f) { fixtureResultViewData.size }
    HorizontalPager(
        modifier = modifier,
        state = state,
        pageSize = PageSize.Fixed(250.dp),
        pageSpacing = 16.dp,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) { index ->
        val match = fixtureResultViewData[index]
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .height(180.dp)
                .width(250.dp)
                .clickable { onFixtureClicked(match.fixtureId) }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(fixtureResultViewData[0].homeTeam.teamLogoUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.team_logo)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "Home team ${match.homeTeam.teamName}",
                    modifier = Modifier
                        .size(logoSize)
                        .weight(0.4f)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.2f)
                ) {
                    Text(
                        text = match.gameState,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    MatchScore(match)
                }

                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(match.awayTeam.teamLogoUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.team_logo)
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = "Away team ${match.awayTeam.teamName}",
                    modifier = Modifier
                        .size(logoSize)
                        .weight(0.4f)
                )
            }
        }
    }
}

@Composable
private fun MatchScore(
    match: FixtureResultViewData,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.h4
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            text = match.homeTeam.goals,
            style = textStyle
        )
        Text(
            text = "-",
            style = textStyle
        )
        Text(
            text = match.awayTeam.goals,
            style = textStyle
        )
    }
}

@Composable
private fun UpcomingFixtures(
    fixtureViewData: ImmutableList<FixturesViewData>,
    modifier: Modifier = Modifier,
    onFixtureClicked: (fixtureId: String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items = fixtureViewData) { fixture ->
            Card(elevation = 4.dp, modifier = Modifier.clickable { onFixtureClicked(fixture.fixtureId)  }) {
                Fixture(fixture, Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
private fun Fixture(
    fixtureViewData: FixturesViewData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HomeTeam(fixtureViewData.homeTeam, Modifier.weight(0.4f))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
        ) {
            Text(
                text = fixtureViewData.date,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(text = fixtureViewData.time, style = MaterialTheme.typography.subtitle2)
        }
        AwayTeam(fixtureViewData.awayTeam, Modifier.weight(0.4f))
    }
}

@Composable
private fun HomeTeam(
    fixtureViewData: TeamFixtureViewData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = fixtureViewData.teamName,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(0.7f)
        )
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(fixtureViewData.teamLogoUrl)
                .crossfade(true)
                .placeholder(R.drawable.team_logo)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = "Home team ${fixtureViewData.teamName}",
            modifier = Modifier
                .size(48.dp)
                .weight(0.3f)
        )
    }
}

@Composable
private fun AwayTeam(
    fixtureViewData: TeamFixtureViewData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(fixtureViewData.teamLogoUrl)
                .crossfade(true)
                .placeholder(R.drawable.team_logo)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = "Away team ${fixtureViewData.teamName}",
            modifier = Modifier
                .size(48.dp)
                .weight(0.3f)
        )
        Text(
            textAlign = TextAlign.Center,
            text = fixtureViewData.teamName,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewContent() {
    Content(TestData.fixtures, TestData.fixtureResults, {})
}

object TestData {
    private val fixture1 = FixturesViewData("2",
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "2",
            "1"
        ),
        TeamFixtureViewData(
            "Manchester United",
            "https://media.api-sports.io/football/teams/33.png",
            "1",
            "2"
        ),
        "2023-09-24",
        "15:00"
    )

    val fixture2 = FixturesViewData("4",
        TeamFixtureViewData(
            "Morocco",
            "https://media.api-sports.io/football/teams/31.png",
            "3",
            "3"
        ),
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "0",
            "4"
        ),
        "2023-09-25",
        "14:30"
    )

    val fixture3 = FixturesViewData("5",
        TeamFixtureViewData(
            "Elite",
            "https://media.api-sports.io/football/teams/68.png",
            "1",
            "5"
        ),
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "2",
            "6"
        ),
        "2023-09-26",
        "16:15"
    )

    val fixture4 = FixturesViewData("7",
        TeamFixtureViewData(
            "Copenhagen",
            "https://media.api-sports.io/football/teams/400.png",
            "0",
            "7"
        ),
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "2",
            "8"
        ),
        "2023-09-27",
        "18:45"
    )

    val fixture5 = FixturesViewData("12",
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "2",
            "9"
        ),
        TeamFixtureViewData(
            "Parana",
            "https://media.api-sports.io/football/teams/122.png",
            "2",
            "10"
        ),
        "2023-09-28",
        "20:00"
    )

    val fixture6 = FixturesViewData("74",
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "1",
            "11"
        ),
        TeamFixtureViewData("Aca", "https://media.api-sports.io/football/teams/98.png", "3", "12"),
        "2023-09-29",
        "19:30"
    )

    val fixture7 = FixturesViewData("233",
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "2",
            "13"
        ),
        TeamFixtureViewData(
            "Reading",
            "https://media.api-sports.io/football/teams/53.png",
            "0",
            "14"
        ),
        "2023-09-30",
        "17:45"
    )

    val fixture8 = FixturesViewData("421",
        TeamFixtureViewData(
            "Saudi Arabia",
            "https://media.api-sports.io/football/teams/23.png",
            "0",
            "15"
        ),
        TeamFixtureViewData(
            "Paris saint Germain",
            "https://media.api-sports.io/football/teams/85.png",
            "1",
            "16"
        ),
        "2023-10-01",
        "16:00"
    )

    val fixtures = persistentListOf(
        fixture1,
        fixture2,
        fixture3,
        fixture4,
        fixture5,
        fixture6,
        fixture7,
        fixture8
    )

    val fixtureResults = persistentListOf(
        FixtureResultViewData(
            "3",
            TeamFixtureViewData(
                "Paris saint Germain",
                "https://media.api-sports.io/football/teams/85.png",
                "2",
                "1"
            ),
            TeamFixtureViewData(
                "Manchester United",
                "https://media.api-sports.io/football/teams/33.png",
                "1",
                "2"
            ),
            "2023-09-24",
            "15:00"
        ),
        FixtureResultViewData(
            "3",
            TeamFixtureViewData(
                "Paris saint Germain",
                "https://media.api-sports.io/football/teams/85.png",
                "2",
                "1"
            ),
            TeamFixtureViewData(
                "Copenhagen",
                "https://media.api-sports.io/football/teams/400.png",
                "1",
                "2"
            ),
            "2023-09-24",
            "Full Time"
        ),
        FixtureResultViewData(
            "3",

            TeamFixtureViewData(
                "Master don",
                "https://media.api-sports.io/football/teams/67.png",
                "2",
                "213"
            ),
            TeamFixtureViewData(
                "Paris saint Germain",
                "https://media.api-sports.io/football/teams/85.png",
                "1",
                "1"
            ),
            "2023-09-12",
            "Full Time"
        )
    )
}