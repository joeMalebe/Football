package com.example.football.presentation.view.composable

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 720,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ErrorDialogPreview() {
    FootballTheme {
        ErrorScreen()
    }
}