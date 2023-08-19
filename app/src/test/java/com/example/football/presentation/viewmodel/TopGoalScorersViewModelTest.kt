package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.FootballService
import com.example.football.data.repository.TestData
import com.example.football.data.repository.TopGoalScorerRepositoryImpl
import com.example.football.domain.TopGoalScorerViewData
import com.example.football.domain.usecase.GetTopGoalScorersUseCase
import com.example.football.domain.usecase.GetTopGoalScorersUseCaseImpl
import com.example.football.domain.usecase.TopGoalScorersResult
import com.example.football.presentation.view.composable.PreviewData
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

    @Mock
    lateinit var mockUseCase: GetTopGoalScorersUseCase

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

    @Test
    fun `When isSeeAll is false emit TopGoalScorersLoaded view state with top 5 scorers`() = runTest {
        whenever(mockUseCase.getTopGoalScorers("1", "2022")).thenReturn(
            TopGoalScorersResult.Loaded(PreviewData.topGoalScorersList)
        )
        val mockViewModel = TopGoalScorersViewModel(mockUseCase, testDispatcher)
        mockViewModel.topGoalScorerViewState.observeForever(observer)

        mockViewModel.loadTopGoalScorers(leagueId = "1", season = "2022")

        verify(observer, times(1)).onChanged(
            TopGoalScorersViewState.TopGoalScorersLoaded(PreviewData.topGoalScorersList.take(5))
        )
        verify(observer, times(0)).onChanged(
            TopGoalScorersViewState.TopGoalScorersLoaded(PreviewData.topGoalScorersList)
        )
    }

    @Test
    fun `When onSeeAllClick is called and isSeeAll emit TopGoalScorersLoaded view state with all scorers`() = runTest {
        whenever(mockUseCase.getTopGoalScorers("1", "2022")).thenReturn(
            TopGoalScorersResult.Loaded(PreviewData.topGoalScorersList)
        )
        val mockViewModel = TopGoalScorersViewModel(mockUseCase, testDispatcher)
        mockViewModel.topGoalScorerViewState.observeForever(observer)

        mockViewModel.loadTopGoalScorers(leagueId = "1", season = "2022")
        mockViewModel.onSeeAllClick()

        verify(observer, times(1)).onChanged(
            TopGoalScorersViewState.TopGoalScorersLoaded(PreviewData.topGoalScorersList.take(5))
        )
        verify(observer, times(1)).onChanged(
            TopGoalScorersViewState.TopGoalScorersLoaded(PreviewData.topGoalScorersList)
        )
    }

    @Test
    fun `When getTopGoalScorers is called with isSeeAll true return all scorers`() {
        val topGoalScorers = viewModel.getTopGoalScorers(PreviewData.topGoalScorersList, true)

        assertEquals(PreviewData.topGoalScorersList, topGoalScorers)
    }

    @Test
    fun `When getTopGoalScorers is called with isSeeAll false return top 5 scorers`() {
        val topGoalScorers = viewModel.getTopGoalScorers(PreviewData.topGoalScorersList, false)

        assertEquals(PreviewData.topGoalScorersList.take(5), topGoalScorers)
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