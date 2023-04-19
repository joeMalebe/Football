package com.example.football.presentation.view.composable

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.CountryViewData
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.viewmodel.HomeScreenViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val nullableViewState by viewModel.searchResultViewState.observeAsState()
    val viewState = nullableViewState ?: return

    Content(
        searchText = searchText.text,
        onSearchTextChange = { newSearchText ->
            searchText = TextFieldValue(newSearchText)
            viewModel.searchOnTextChanged(newSearchText)
        },
        viewState = viewState
    )
}

@Composable
fun Content(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewState: HomeScreenViewModel.SearchViewState
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
                CountriesList(countries = viewState.countries, modifier = Modifier.padding(it))
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
            else -> {
                Text(text = "welcome home", style = MaterialTheme.typography.h5)
            }
        }
    }
}

@Composable
fun CountriesList(countries: List<CountryViewData>, modifier: Modifier = Modifier) {
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
                .widthIn(min = 48.dp, max = 144.dp)
                .heightIn(min = 40.dp, max = 138.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = country.name,
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
        CountriesList(countries = PreviewData.countries)
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
            viewState = HomeScreenViewModel.SearchViewState.InitialViewState
        )
    }
}

object PreviewData {
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
}
