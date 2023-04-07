package com.example.football.presentation.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.football.R
import com.example.football.presentation.theme.FootballTheme

@Composable
fun HomeScreen() {

    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Content(
        searchText = searchText.text,
        onSearchTextChange = { newSearchText ->
            searchText = TextFieldValue(newSearchText)
        })
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
                modifier = Modifier.fillMaxWidth(),
                searchText = searchText,
                onSearchTextChange = onSearchTextChange
            )
        }
    ) {
        it
    }
}

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        modifier = modifier,
        shape = CircleShape,
        placeholder = {
            Text(
                text = stringResource(id = R.string.search)
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search") }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchBarPreview() {
    FootballTheme {
        SearchBar(searchText = "", onSearchTextChange = {})
    }
}
