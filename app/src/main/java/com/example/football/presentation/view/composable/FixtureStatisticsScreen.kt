package com.example.football.presentation.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FixtureStatisticsScreen(modifier: Modifier = Modifier) {

}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hello")
        Text(text = "World")
    }
}

@Composable
@Preview
private fun Preview() {
    Content()
}