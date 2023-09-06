package com.example.football.presentation.viewmodel

import android.content.Context
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
import com.example.football.exception.ResultCallAdaptorFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class PlayerStatisticsViewModelTest {

    private lateinit var footballService: FootballService
    private val playerStatisticsRepository by lazy { PlayerStatisticsRepositoryImpl(footballService) }
    private val playerStatisticsUseCase by lazy {
        PlayerStatisticsUseCaseImpl(
            playerStatisticsRepository
        )
    }
    private val testDispatcher = UnconfinedTestDispatcher()
    private val okHttpClient = OkHttpClient()
    private val mockWebServer = MockWebServer()
    private val context = ApplicationProvider.getApplicationContext<Context>()

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
        mockWebServer.start()
        footballService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdaptorFactory())
            .client(okHttpClient)
            .build()
            .create(FootballService::class.java)

    }


    @Test
    fun `When player stats loads player data successfully then return success view state`() {
        val playerStatisticsDto = readJsonFromRawResource(context, R.raw.player_stats_response)
        mockWebServer.enqueue(
            MockResponse().setResponseCode(200)
                .setBody(getJsonStringFromResource(context, R.raw.player_stats_response))
        )
        playerStatisticsViewModel.playerStatisticsViewState.observeForever(observer)

        playerStatisticsViewModel.getPlayerStatistics(1, "2020")

        verify(observer).onChanged(PlayerStatisticsViewState.Loading)
        verify(observer).onChanged(PlayerStatisticsViewState.Success(PreviewData.playerStatistics))
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
            playerRating = 85
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
                competition = "Premier League",
                competitionImageUrl = "https://media.api-sports.io/football/leagues/61.png",
                team = "Paris Saint Germain",
                teamLogoUrl = "https://media.api-sports.io/football/teams/85.png",
                yellowCards = 5
            ), StatisticsViewDate(
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
                yellowCards = 5
            ), StatisticsViewDate(
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
            )
        )
    )
    }
}