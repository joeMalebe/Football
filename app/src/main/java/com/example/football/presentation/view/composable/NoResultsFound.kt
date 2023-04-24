package com.example.football.presentation.view.composable

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.football.R
import com.example.football.presentation.theme.FootballTheme

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

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 720,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun NoResultsFoundPreview() {
    FootballTheme {
        NoResultsFound()
    }
}
