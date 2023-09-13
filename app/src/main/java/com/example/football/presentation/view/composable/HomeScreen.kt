package com.example.football.presentation.view.composable

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.CountryViewData
import com.example.football.domain.LeagueViewData
import com.example.football.domain.PlayerInfoViewData
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.StandingsViewData
import com.example.football.domain.StatisticsViewDate
import com.example.football.domain.TopGoalScorerViewData
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.viewmodel.HomeScreenViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, navController: NavController) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val nullableViewState by viewModel.searchResultViewState.observeAsState()
    val viewState = nullableViewState ?: return

    Content(
        searchText = searchText.text,
        onSearchTextChange = { newSearchText ->
            searchText = TextFieldValue(newSearchText)
            viewModel.searchLeagueOnTextChanged(newSearchText)
        },
        viewState = viewState,
        onLeagueItemClicked = { league ->
            navController.navigate(
                resId = R.id.standingsFragment,
                args = Bundle().apply { putString("leagueId", league.id.toString()) }
            )
            Log.i("HomeScreen", "league clicked $league")
        }
    )
}

@Composable
fun Content(
    searchText: String,
    viewState: HomeScreenViewModel.SearchViewState,
    onSearchTextChange: (String) -> Unit,
    onLeagueItemClicked: (LeagueViewData) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                searchText = searchText,
                onSearchTextChange = onSearchTextChange
            )
        }
    ) {
        when (viewState) {
            is HomeScreenViewModel.SearchViewState.CountrySearchResults -> {
                CountriesList(
                    countries = viewState.countries.toImmutableList(),
                    modifier = Modifier.padding(it)
                )
            }
            is HomeScreenViewModel.SearchViewState.Loading -> {
                Loading()
            }
            is HomeScreenViewModel.SearchViewState.Error -> {
                ErrorScreen()
            }
            is HomeScreenViewModel.SearchViewState.NoSearchResults -> {
                NoResultsFound()
            }
            is HomeScreenViewModel.SearchViewState.LeagueSearchResults -> {
                LeagueList(
                    leagues = viewState.leagues.toImmutableList(),
                    modifier = Modifier.padding(it),
                    onLeagueItemClicked = onLeagueItemClicked
                )
            }
            else -> {
                Text(text = "welcome home", style = MaterialTheme.typography.h5)
            }
        }
    }
}

@Composable
fun CountriesList(countries: ImmutableList<CountryViewData>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2), content = {
        items(count = countries.size) { index ->
            CountryViewItem(
                country = countries[index],
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    })
}

@Composable
fun CountryViewItem(country: CountryViewData, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(country.flagUri)
                .crossfade(true)
                .decoderFactory(SvgDecoder.Factory())
                .build(),
            contentDescription = country.name,
            placeholder = painterResource(
                id = R.drawable.ic_flag_placeholder
            ),
            modifier = Modifier
                .width(144.dp)
                .height(96.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = country.name,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun LeagueList(
    leagues: ImmutableList<LeagueViewData>,
    modifier: Modifier = Modifier,
    onLeagueItemClicked: (LeagueViewData) -> Unit
) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(2), content = {
        items(count = leagues.size) { index ->
            LeagueViewItem(
                league = leagues[index],
                modifier = Modifier
                    .padding(all = 16.dp)
                    .border(width = 3.dp, color = Color.Blue)
                    .clickable { onLeagueItemClicked(leagues[index]) }
            )
        }
    })
}

@Composable
fun LeagueViewItem(league: LeagueViewData, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(league.logo)
                .crossfade(true)
                .build(),
            contentDescription = league.name,
            placeholder = painterResource(
                id = R.drawable.ic_flag_placeholder
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(96.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            textAlign = TextAlign.Center,
            text = league.name,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CountryViewItemPreview() {
    FootballTheme {
        CountryViewItem(PreviewData.countries[2])
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CountriesListPreview() {
    FootballTheme {
        CountriesList(countries = PreviewData.countries.toImmutableList())
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ContentPreview() {
    FootballTheme {
        Content(
            searchText = "",
            onSearchTextChange = {},
            viewState = HomeScreenViewModel.SearchViewState.InitialViewState,
            onLeagueItemClicked = {}
        )
    }
}

object PreviewData {
    val playerStatistics = PlayerStatisticsViewData(
        playerInfoViewData = PlayerInfoViewData(
            fullName = "Neymar da Silva Santos Júnior",
            surname = "da Silva Santos Júnior",
            age = 29,
            weight = "68 kg",
            imageUrl = "https://media.api-sports.io/football/players/276.png",
            playerRating = 80
        ),
        statisticsViewDate = persistentListOf(
            StatisticsViewDate(
                games = 13,
                shots = 39,
                goals = 6,
                assists = 3,
                passes = 610,
                tackles = 8,
                duelsWon = 122,
                dribblesCompleted = 60,
                fouls = 22,
                redCards = 1,
                competition = "Ligue 1",
                competitionImageUrl = "https://media.api-sports.io/football/leagues/61.png",
                team = "Paris Saint Germain",
                teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                yellowCards = 5
            ),
            StatisticsViewDate(
                games = 1,
                shots = 1,
                goals = 0,
                assists = 1,
                passes = 26,
                tackles = 0,
                duelsWon = 13,
                dribblesCompleted = 7,
                fouls = 5,
                redCards = 0,
                competition = "Coupe de France",
                competitionImageUrl = "https://media.api-sports.io/football/leagues/66.png",
                team = "Paris Saint Germain",
                teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                yellowCards = 7
            ),
            StatisticsViewDate(
                games = 5,
                shots = 13,
                goals = 6,
                assists = 0,
                passes = 194,
                tackles = 3,
                duelsWon = 51,
                dribblesCompleted = 21,
                fouls = 5,
                redCards = 0,
                competition = "UEFA Champions League",
                competitionImageUrl = "https://media.api-sports.io/football/leagues/2.png",
                team = "Paris Saint Germain",
                teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                yellowCards = 2
            ),
            StatisticsViewDate(
                games = 2,
                shots = 0,
                goals = 3,
                assists = 0,
                passes = 0,
                tackles = 0,
                duelsWon = 0,
                dribblesCompleted = 0,
                fouls = 0,
                redCards = 0,
                competition = "Club Friendlies",
                competitionImageUrl = "",
                team = "Paris Saint Germain",
                teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                yellowCards = 0
            )
        )
    )

    val topGoalScorersList = listOf(
        TopGoalScorerViewData(1, "Lionel", "Messi", 35, "https://example.com/messi.jpg"),
        TopGoalScorerViewData(2, "Cristiano", "Ronaldo", 31, "https://example.com/ronaldo.jpg"),
        TopGoalScorerViewData(
            3,
            "Robert",
            "Lewandowski",
            29,
            "https://example.com/lewandowski.jpg"
        ),
        TopGoalScorerViewData(4, "Karim", "Benzema", 27, "https://example.com/benzema.jpg"),
        TopGoalScorerViewData(5, "Erling", "Haaland", 26, "https://example.com/haaland.jpg"),
        TopGoalScorerViewData(6, "Kylian", "Mbappe", 24, "https://example.com/mbappe.jpg"),
        TopGoalScorerViewData(7, "Harry", "Kane", 23, "https://example.com/kane.jpg"),
        TopGoalScorerViewData(8, "Mohamed", "Salah", 22, "https://example.com/salah.jpg"),
        TopGoalScorerViewData(9, "Sergio", "Aguero", 20, "https://example.com/aguero.jpg"),
        TopGoalScorerViewData(10, "Antoine", "Griezmann", 19, "https://example.com/griezmann.jpg")
    )

    val countries = listOf(
        CountryViewData(
            name = "England",
            flagUri = "https://media-2.api-sports.io/flags/gb.svg",
            ""
        ),
        CountryViewData(
            name = "France",
            flagUri = "https://media-2.api-sports.io/flags/fr.svg",
            ""
        ),
        CountryViewData(
            name = "Brazil",
            flagUri = "https://media-2.api-sports.io/flags/br.svg",
            ""
        ),
        CountryViewData(name = "Italy", flagUri = "https://media-2.api-sports.io/flags/it.svg", ""),
        CountryViewData(name = "Spain", flagUri = "https://media-2.api-sports.io/flags/es.svg", "")
    )
    val standings = persistentListOf(
        StandingsViewData(
            1,
            1234,
            "https://example.com/logo.png",
            20,
            3,
            7,
            67,
            "1st Place",
            "Team A"
        ),
        StandingsViewData(
            2,
            5678,
            "https://example.com/logo.png",
            18,
            5,
            7,
            61,
            "2nd Place",
            "Team B"
        ),
        StandingsViewData(3, 9012, null, 15, 8, 7, 52, "3rd Place", "Team C"),
        StandingsViewData(4, 3456, "https://example.com/logo.png", 14, 9, 7, 49, null, "Team D"),
        StandingsViewData(5, 7890, "https://example.com/logo.png", 13, 10, 7, 46, null, "Team E"),
        StandingsViewData(6, 2345, null, 12, 12, 6, 42, null, "Team F"),
        StandingsViewData(7, 6789, "https://example.com/logo.png", 10, 12, 8, 38, null, "Team G"),
        StandingsViewData(8, 1234, null, 9, 13, 8, 35, null, "Team H"),
        StandingsViewData(9, 5678, "https://example.com/logo.png", 8, 15, 7, 31, null, "Team I"),
        StandingsViewData(10, 9012, null, 6, 16, 8, 26, null, "Team J"),
        StandingsViewData(11, 3456, "https://example.com/logo.png", 5, 18, 7, 22, null, "Team K"),
        StandingsViewData(12, 7890, "https://example.com/logo.png", 4, 19, 7, 19, null, "Team L"),
        StandingsViewData(13, 2345, null, 3, 20, 7, 16, null, "Team M"),
        StandingsViewData(14, 6789, "https://example.com/logo.png", 2, 21, 7, 13, null, "Team N"),
        StandingsViewData(15, 1234, null, 1, 22, 7, 10, null, "Team O"),
        StandingsViewData(16, 5678, "https://example.com/logo.png", 0, 23, 7, 7, null, "Team P"),
        StandingsViewData(17, 9012, null, 0, 23, 7, 7, null, "Team Q"),
        StandingsViewData(18, 3456, "https://example.com/logo.png", 0, 23, 7, 7, null, "Team R"),
        StandingsViewData(19, 7890, "https://example.com/logo.png", 0, 23, 7, 7, null, "Team")
    )
}
