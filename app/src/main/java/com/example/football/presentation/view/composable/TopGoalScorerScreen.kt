package com.example.football.presentation.view.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.football.R
import com.example.football.domain.TopGoalScorerViewData
import com.example.football.presentation.viewmodel.TopGoalScorersViewModel
import com.example.football.presentation.viewmodel.viewstate.TopGoalScorersViewState

@Composable
fun TopGoalScorerScreen(viewModel: TopGoalScorersViewModel) {
    val nullableViewState by
        viewModel.topGoalScorerViewState.observeAsState()

    val viewState = nullableViewState ?: return
    Scaffold { paddingValues ->
        when (viewState) {
            is TopGoalScorersViewState.TopGoalScorersLoaded -> {
                Content(
                    topGoalScorerViewData = viewState.topGoalScorers,
                    modifier = Modifier.padding(paddingValues),
                    onSeeAllClick = { viewModel.onSeeAllClick() }
                )
            }
            is TopGoalScorersViewState.Loading -> {
                Loading()
            }
            else -> {
                Text(text = "Hello")
            }
        }
    }
}

@Composable
fun Content(
    topGoalScorerViewData: List<TopGoalScorerViewData>,
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth().animateContentSize(animationSpec = tween(2000))) {
        TopScorersTable(
            goalScorerViewDataList = topGoalScorerViewData,
            onSeeAllClick = onSeeAllClick
        )
    }
}

@Composable
fun RowScope.TablePlayerItemCell(
    topGoalScorerViewData: TopGoalScorerViewData,
    weight: Float,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .weight(weight)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .placeholder(R.drawable.ic_player_placeholder).crossfade(true)
                .data(topGoalScorerViewData.playerImgUrl).build(),
            contentDescription = topGoalScorerViewData.playerFullName,
            modifier = Modifier
                .padding(end = 8.dp)
                .size(36.dp)
                .clip(CircleShape)
        )

        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = topGoalScorerViewData.playerFullName,
            style = MaterialTheme.typography.body1
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopScorersTable(
    modifier: Modifier = Modifier,
    goalScorerViewDataList: List<TopGoalScorerViewData>,
    onSeeAllClick: () -> Unit
) {
// weight for team name column and stats column
    val columnPlayerNameWeight = .8f // 80%
    val columnPTopScorersHeaderWeight = .8f // 80%
    val columnSeeAllHeaderWeight = .2f // 20%
    val columnGoalsWeight = .2f // 20%
    val columnHeaderWeight = .5f // 50%

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        // Here is the first header row
        item {
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = stringResource(id = R.string.top_scorers),
                    weight = columnPTopScorersHeaderWeight,
                    style = MaterialTheme.typography.h6,
                    contentAlignment = Alignment.CenterStart
                )
                TableCell(
                    text = stringResource(id = R.string.see_all),
                    weight = columnSeeAllHeaderWeight,
                    style = MaterialTheme.typography.subtitle1,
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }
        }

        item {
            // here is the second header row
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = stringResource(id = R.string.player_name),
                    weight = columnHeaderWeight,
                    style = MaterialTheme.typography.subtitle1,
                    contentAlignment = Alignment.CenterStart
                )

                TableCell(
                    text = stringResource(id = R.string.goals),
                    weight = columnGoalsWeight,
                    style = MaterialTheme.typography.subtitle1,
                    contentAlignment = Alignment.CenterEnd
                )
            }
        }

        // Here are all the lines of the table.
        itemsIndexed(items = goalScorerViewDataList, key = { _, player -> player.playerId }) { _, player ->

            // val player = listItem[index]
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TablePlayerItemCell(
                    topGoalScorerViewData = player,
                    weight = columnPlayerNameWeight
                )

                TableCell(
                    text = player.numberOfGoals.toString(),
                    weight = columnGoalsWeight,
                    style = MaterialTheme.typography.body1,
                    contentAlignment = Alignment.CenterEnd
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun TopScorerTablePreview() {
    Content(topGoalScorerViewData = PreviewData.topGoalScorersList, onSeeAllClick = {})
}
