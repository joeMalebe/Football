package com.example.football.presentation.view.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.viewmodel.PlayerStatisticsViewModel

@Composable
fun PkayerStatsScreen(
    viewModel: PlayerStatisticsViewModel,
    findNavController: NavController,
    modifier: Modifier = Modifier
) {

}

@Composable
private fun Content(playerStatisticsViewData: PlayerStatisticsViewData, modifier: Modifier = Modifier) {
    Row(modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(playerStatisticsViewData.playerInfoViewData.imageUrl)
                .placeholder(R.drawable.ic_player_placeholder)
                .crossfade(true)
                .build(), contentDescription = "Player Image"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FootballTheme {
        Content(playerStatisticsViewData = PreviewData.playerStatistics)
    }
}