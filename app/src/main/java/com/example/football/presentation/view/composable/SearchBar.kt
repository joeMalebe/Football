package com.example.football.presentation.view.composable

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.football.R
import com.example.football.presentation.theme.FootballTheme

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

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    FootballTheme {
        SearchBar(searchText = "", onSearchTextChange = {})
    }
}
