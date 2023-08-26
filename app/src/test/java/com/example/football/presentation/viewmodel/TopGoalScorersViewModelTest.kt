package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.FootballService
import com.example.football.data.repository.TestData
import com.example.football.data.repository.TopGoalScorerRepositoryImpl
import com.example.football.domain.TopGoalScorerViewData
import com.example.football.domain.usecase.GetTopGoalScorersUseCase
import com.example.football.domain.usecase.GetTopGoalScorersUseCaseImpl
import com.example.football.presentation.viewmodel.viewstate.TopGoalScorersViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class TopGoalScorersViewModelTest {
    @Mock
    lateinit var footballService: FootballService

    @Mock
    lateinit var observer: Observer<TopGoalScorersViewState>

    private val testDispatcher = UnconfinedTestDispatcher(scheduler = TestCoroutineScheduler())
    private lateinit var useCase: GetTopGoalScorersUseCase
    private lateinit var viewModel: TopGoalScorersViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        useCase = GetTopGoalScorersUseCaseImpl(
            TopGoalScorerRepositoryImpl(
                footballService,
                testDispatcher
            )
        )
        viewModel = TopGoalScorersViewModel(useCase, testDispatcher)
    }

    @Test
    fun `When getting top goal scorers fail emit Error view state`() = runTest {
        whenever(footballService.getTopScorers("3", "2023")).thenReturn(
            Result.failure(
                RuntimeException("error")
            )
        )
        val argumentCaptor = argumentCaptor<TopGoalScorersViewState>()
        viewModel.topGoalScorerViewState.observeForever(observer)

        viewModel.loadTopGoalScorers(leagueId = "3", season = "2023")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(TopGoalScorersViewState.Initial, firstValue)
            assertEquals(TopGoalScorersViewState.Loading, secondValue)
            assertEquals(TopGoalScorersViewState.Error, thirdValue)
        }
    }

    @Test
    fun `When getting top goal scorers success emit TopGoalScorersLoaded view state`() = runTest {
        whenever(footballService.getTopScorers("1", "2022")).thenReturn(
            Result.success(TestData.topGoalScorerResponse)
        )
        val argumentCaptor = argumentCaptor<TopGoalScorersViewState>()
        viewModel.topGoalScorerViewState.observeForever(observer)

        viewModel.loadTopGoalScorers(leagueId = "1", season = "2022")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(TopGoalScorersViewState.Initial, firstValue)
            assertEquals(TopGoalScorersViewState.Loading, secondValue)
            assertEquals(
                TopGoalScorersViewState.TopGoalScorersLoaded(TestViewData.mockTopGoalScorers),
                thirdValue
            )
        }
    }

    @Test
    fun `When getting top goal scorers success but empty emit NoInformation view state`() =
        runTest {
            whenever(footballService.getTopScorers("33", "2010")).thenReturn(
                Result.success(TestData.emptyTopGoalScorerResponse)
            )
            val argumentCaptor = argumentCaptor<TopGoalScorersViewState>()
            viewModel.topGoalScorerViewState.observeForever(observer)

            viewModel.loadTopGoalScorers(leagueId = "33", season = "2010")

            argumentCaptor.run {
                verify(observer, times(3)).onChanged(capture())
                assertEquals(TopGoalScorersViewState.Initial, firstValue)
                assertEquals(TopGoalScorersViewState.Loading, secondValue)
                assertEquals(TopGoalScorersViewState.NoInformation, thirdValue)
            }
        }

    @Test
    fun `When getting top goal scorers success but response is null emit NoInformation view state`() =
        runTest {
            whenever(footballService.getTopScorers("33", "2010")).thenReturn(
                Result.success(TestData.nullTopGoalScorerResponse)
            )
            val argumentCaptor = argumentCaptor<TopGoalScorersViewState>()
            viewModel.topGoalScorerViewState.observeForever(observer)

            viewModel.loadTopGoalScorers(leagueId = "33", season = "2010")

            argumentCaptor.run {
                verify(observer, times(3)).onChanged(capture())
                assertEquals(TopGoalScorersViewState.Initial, firstValue)
                assertEquals(TopGoalScorersViewState.Loading, secondValue)
                assertEquals(TopGoalScorersViewState.NoInformation, thirdValue)
            }
        }

    @Test
    fun `When getting top goal scorers success but response data is null emit TopGoalScorersLoaded view state with default values`() =
        runTest {
            whenever(footballService.getTopScorers("33", "2010")).thenReturn(
                Result.success(TestData.nullPlayerDataTopGoalScorerResponse)
            )
            val argumentCaptor = argumentCaptor<TopGoalScorersViewState>()
            viewModel.topGoalScorerViewState.observeForever(observer)

            viewModel.loadTopGoalScorers(leagueId = "33", season = "2010")

            argumentCaptor.run {
                verify(observer, times(3)).onChanged(capture())
                assertEquals(TopGoalScorersViewState.Initial, firstValue)
                assertEquals(TopGoalScorersViewState.Loading, secondValue)
                assertEquals(
                    TopGoalScorersViewState.TopGoalScorersLoaded(
                        TestViewData.defaultTopGoalScorerViewData
                    ),
                    thirdValue
                )
            }
        }
}

object TestViewData {
    val defaultTopGoalScorerViewData = listOf(
        TopGoalScorerViewData(
            playerId = 0,
            playerName = "",
            playerSurname = "",
            numberOfGoals = 0,
            playerImgUrl = ""
        )
    )

    val mockTopGoalScorers: List<TopGoalScorerViewData> = listOf(
        TopGoalScorerViewData(
            playerId = 1,
            playerName = "Lionel",
            playerSurname = "Messi",
            numberOfGoals = 23,
            playerImgUrl = "https://media.api-sports.io/football/players/1.png"
        ),
        TopGoalScorerViewData(
            playerId = 2,
            playerName = "Cristiano",
            playerSurname = "Ronaldo",
            numberOfGoals = 15,
            playerImgUrl = "https://media.api-sports.io/football/players/2.png"
        )
    )
}