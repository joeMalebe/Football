package com.example.football.presentation.view

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.CountryViewData
import com.example.football.presentation.theme.FootballTheme

@Composable
fun HomeScreen() {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Content(
        searchText = searchText.text,
        onSearchTextChange = { newSearchText ->
            searchText = TextFieldValue(newSearchText)
        }
    )
}

@Composable
fun Content(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                searchText = searchText,
                onSearchTextChange = onSearchTextChange
            )
        }
    ) {
        it.calculateBottomPadding()
        CountriesList(countries = PreviewData.countries)
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier,
        shape = CircleShape,
        maxLines = 1,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search)
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") }
    )
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

@Composable
fun Loading(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(Modifier.size(72.dp))
    }
}

@Composable
fun NoResultsFound(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_results),
            contentDescription = "No search results",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .heightIn(min = 144.dp, max = 360.dp)
                .widthIn(min = 48.dp, max = 96.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.no_results_found),
            fontWeight = FontWeight(700),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = stringResource(id = R.string.we_cant_find_any_results),
            style = MaterialTheme.typography.subtitle1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_referee),
            contentDescription = "An error has occurred",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .heightIn(min = 120.dp, max = 384.dp)
                .widthIn(min = 48.dp, max = 220.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            text = stringResource(id = R.string.an_error_has_occurred),
            style = MaterialTheme.typography.h5,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(text = stringResource(id = R.string.try_again), modifier = Modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    FootballTheme {
        SearchBar(searchText = "", onSearchTextChange = {})
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

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun LoadingPreview() {
    FootballTheme {
        Loading()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ErrorDialogPreview() {
    FootballTheme {
        ErrorScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun NoResultsFoundPreview() {
    FootballTheme {
        NoResultsFound()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, widthDp = 360, heightDp = 720, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun ContentPreview() {
    FootballTheme {
        Content("", {})
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
