package com.example.football.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.example.football.FootballService
import com.example.football.R
import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.repository.PlayerStatisticsRepositoryImpl
import com.example.football.domain.usecase.PlayerStatisticsUseCaseImpl
import com.example.football.presentation.view.composable.PreviewData.playerStatistics
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
                    playerStatistics
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
}