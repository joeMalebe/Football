package com.example.football.presentation.view.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.StatisticsViewDate
import com.example.football.presentation.theme.FootballTheme
import com.example.football.presentation.viewmodel.PlayerStatisticsViewModel
import kotlin.math.min
import kotlin.math.sqrt
import okhttp3.internal.immutableListOf

@Composable
fun PkayerStatsScreen(
    viewModel: PlayerStatisticsViewModel,
    findNavController: NavController,
    modifier: Modifier = Modifier
) {
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    playerStatisticsViewData: PlayerStatisticsViewData,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        PlayerInfoView(playerStatisticsViewData)

        PlayerStatisticsList(playerStatisticsViewData)
    }
}

@Composable
private fun PlayerInfoView(
    playerStatisticsViewData: PlayerStatisticsViewData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        PlayerInfo(playerStatisticsViewData)
        PlayerRating(playerStatisticsViewData.playerInfoViewData.playerRating.toString())
    }
}

@Composable
private fun PlayerInfo(playerStatisticsViewData: PlayerStatisticsViewData) {
    Row {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(playerStatisticsViewData.playerInfoViewData.imageUrl)
                .placeholder(R.drawable.ic_player_placeholder)
                .crossfade(true)
                .build(),
            contentDescription = "Player Image",
            modifier = Modifier
                .width(72.dp)
                .height(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Text(
            text = playerStatisticsViewData.playerInfoViewData.fullName,

            modifier = Modifier
                .width(190.dp)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.h6,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Bold

        )
    }
}

@Composable
private fun PlayerRating(rating: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = HexagonShape()
                )
                .size(64.dp)

        ) {
            Text(
                text = rating,
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    shape = HexagonShape()
                )
                .size(80.dp)
        ) {}
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun PlayerStatisticsList(playerStatisticsViewData: PlayerStatisticsViewData) {
    LazyColumn() {
        items(playerStatisticsViewData.statisticsViewDate) { stats ->
            Text(
                text = stats.competition,
                Modifier.padding(top = 24.dp, bottom = 8.dp),
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )

            val statsLabelAndValue = getStatsData(stats)

            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(3),
                horizontalItemSpacing = 16.dp,
                modifier = Modifier
                    .height(104.dp)
                    .fillMaxWidth()
            ) {
                items(statsLabelAndValue) { item ->
                    Row(Modifier.padding(vertical = 8.dp)) {
                        Text(text = "${item.first} ", style = MaterialTheme.typography.body2)
                        Text(
                            text = item.second.toString(),
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun getStatsData(stats: StatisticsViewDate) = immutableListOf(
    Pair("Games", stats.games),
    Pair("Goals scored", stats.goals),
    Pair("Assists", stats.assists),
    Pair("Duels won", stats.duelsWon),
    Pair("Fouls", stats.fouls),
    Pair("Completed dribbles", stats.dribblesCompleted)
)

@Preview(showBackground = true)
@Composable
private fun Preview() {
    FootballTheme {
        Content(
            playerStatisticsViewData = PreviewData.playerStatistics,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

class HexagonShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawCustomHexagonPath(size)
        )
    }
}

fun drawCustomHexagonPath(size: Size): Path {
    return Path().apply {
        val radius = min(size.width / 2f, size.height / 2f)
        customHexagon(radius, size)
    }
}

fun Path.customHexagon(radius: Float, size: Size) {
    val triangleHeight = (sqrt(3.0) * radius / 2)
    val centerX = size.width / 2
    val centerY = size.height / 2

    moveTo(centerX, centerY + radius)
    lineTo((centerX - triangleHeight).toFloat(), centerY + radius / 2)
    lineTo((centerX - triangleHeight).toFloat(), centerY - radius / 2)
    lineTo(centerX, centerY - radius)
    lineTo((centerX + triangleHeight).toFloat(), centerY - radius / 2)
    lineTo((centerX + triangleHeight).toFloat(), centerY + radius / 2)

    close()
}
