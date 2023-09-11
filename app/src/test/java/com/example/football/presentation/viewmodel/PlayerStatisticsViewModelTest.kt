package com.example.football.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.example.football.FootballService
import com.example.football.R
import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.repository.PlayerStatisticsRepositoryImpl
import com.example.football.domain.PlayerInfoViewData
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.StatisticsViewDate
import com.example.football.domain.usecase.PlayerStatisticsUseCaseImpl
import java.lang.RuntimeException
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PlayerStatisticsViewModelTest {

    @Mock
    private lateinit var footballService: FootballService
    private val playerStatisticsRepository by lazy {
        PlayerStatisticsRepositoryImpl(
            footballService,
            testDispatcher
        )
    }
    private val playerStatisticsUseCase by lazy {
        PlayerStatisticsUseCaseImpl(
            playerStatisticsRepository
        )
    }
    private val testDispatcher = UnconfinedTestDispatcher(scheduler = TestCoroutineScheduler())
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<PlayerStatisticsViewState>
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val playerStatisticsViewModel: PlayerStatisticsViewModel by lazy {
        PlayerStatisticsViewModel(playerStatisticsUseCase, testDispatcher)
    }

    @Before
    fun setUp() {
        openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `When player stats loads player data successfully then return success view state`() = runTest {
        whenever(footballService.getPlayer("1", "2020")).thenReturn(
            Result.success(
                readJsonFromRawResource(
                    context,
                    R.raw.player_stats_response
                )!!
            )
        )
        val argumentCaptor = argumentCaptor<PlayerStatisticsViewState>()
        playerStatisticsViewModel.playerStatisticsViewState.observeForever(observer)

        playerStatisticsViewModel.getPlayerStatistics("1", "2020")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                PlayerStatisticsViewState.Loading,
                firstValue
            )
            assertEquals(
                PlayerStatisticsViewState.Success(
                    PreviewData.playerStatistics
                ),
                secondValue
            )
        }
    }

    @Test
    fun `When player stats loads player data with error then return error view state`() = runTest {
        whenever(footballService.getPlayer("66", "2023")).thenThrow(RuntimeException("Error"))
        val argumentCaptor = argumentCaptor<PlayerStatisticsViewState>()
        playerStatisticsViewModel.playerStatisticsViewState.observeForever(observer)

        playerStatisticsViewModel.getPlayerStatistics("66", "2023")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                PlayerStatisticsViewState.Loading,
                firstValue
            )
            assertEquals(
                PlayerStatisticsViewState.Error,
                secondValue
            )
        }
    }

    @Test
    fun `When player stats loads empty response then emit no information view state`() = runTest {
        whenever(footballService.getPlayer("3", "2022")).thenReturn(
            Result.success(readJsonFromRawResource(context, R.raw.player_stats_empty_response)!!)
        )
        val argumentCaptor = argumentCaptor<PlayerStatisticsViewState>()
        playerStatisticsViewModel.playerStatisticsViewState.observeForever(observer)

        playerStatisticsViewModel.getPlayerStatistics("3", "2022")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                PlayerStatisticsViewState.Loading,
                firstValue
            )
            assertEquals(
                PlayerStatisticsViewState.NoInformation,
                secondValue
            )
        }
    }

    @Test
    fun `When player stats loads null response then emit no information view state`() = runTest {
        whenever(footballService.getPlayer("41", "2016")).thenReturn(
            Result.success(readJsonFromRawResource(context, R.raw.player_stats_empty_response)!!)
        )
        val argumentCaptor = argumentCaptor<PlayerStatisticsViewState>()
        playerStatisticsViewModel.playerStatisticsViewState.observeForever(observer)

        playerStatisticsViewModel.getPlayerStatistics("41", "2016")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                PlayerStatisticsViewState.Loading,
                firstValue
            )
            assertEquals(
                PlayerStatisticsViewState.NoInformation,
                secondValue
            )
        }
    }

    private fun readJsonFromRawResource(context: Context, resourceId: Int): PlayerStatisticsDto? {
        return try {
            val jsonText = getJsonStringFromResource(context, resourceId)
            json.decodeFromString<PlayerStatisticsDto>(jsonText)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getJsonStringFromResource(context: Context, resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }

    object PreviewData {
        val playerStatistics = PlayerStatisticsViewData(
            playerInfoViewData = PlayerInfoViewData(
                fullName = "Neymar da Silva Santos Júnior",
                surname = "da Silva Santos Júnior",
                age = 29,
                weight = "68 kg",
                imageUrl = "https://media.api-sports.io/football/players/276.png",
                playerRating = 80
            ),
            statisticsViewDate = persistentListOf(
                StatisticsViewDate(
                    games = 13,
                    shots = 39,
                    goals = 6,
                    assists = 3,
                    passes = 610,
                    tackles = 8,
                    duelsWon = 122,
                    dribblesCompleted = 60,
                    fouls = 22,
                    redCards = 1,
                    competition = "Ligue 1",
                    competitionImageUrl = "https://media.api-sports.io/football/leagues/61.png",
                    team = "Paris Saint Germain",
                    teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                    yellowCards = 5
                ),
                StatisticsViewDate(
                    games = 1,
                    shots = 1,
                    goals = 0,
                    assists = 1,
                    passes = 26,
                    tackles = 0,
                    duelsWon = 13,
                    dribblesCompleted = 7,
                    fouls = 5,
                    redCards = 0,
                    competition = "Coupe de France",
                    competitionImageUrl = "https://media.api-sports.io/football/leagues/66.png",
                    team = "Paris Saint Germain",
                    teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                    yellowCards = 7
                ),
                StatisticsViewDate(
                    games = 5,
                    shots = 13,
                    goals = 6,
                    assists = 0,
                    passes = 194,
                    tackles = 3,
                    duelsWon = 51,
                    dribblesCompleted = 21,
                    fouls = 5,
                    redCards = 0,
                    competition = "UEFA Champions League",
                    competitionImageUrl = "https://media.api-sports.io/football/leagues/2.png",
                    team = "Paris Saint Germain",
                    teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                    yellowCards = 2
                ),
                StatisticsViewDate(
                    games = 2,
                    shots = 0,
                    goals = 3,
                    assists = 0,
                    passes = 0,
                    tackles = 0,
                    duelsWon = 0,
                    dribblesCompleted = 0,
                    fouls = 0,
                    redCards = 0,
                    competition = "Club Friendlies",
                    competitionImageUrl = "",
                    team = "Paris Saint Germain",
                    teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                    yellowCards = 0
                )
            )
        )
    }
}