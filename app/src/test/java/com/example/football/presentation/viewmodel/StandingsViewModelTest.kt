package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.FootballService
import com.example.football.data.repository.StandingsRepository
import com.example.football.data.repository.StandingsRepositoryImpl
import com.example.football.domain.StandingsResult
import com.example.football.domain.StandingsViewDataMapperImpl
import com.example.football.domain.usecase.StandingsUseCase
import com.example.football.domain.usecase.StandingsUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.yield
import org.junit.After
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
class StandingsViewModelTest {

    private lateinit var standingsViewModel: StandingsViewModel
    private lateinit var standingsUseCase: StandingsUseCase
    private lateinit var standingsRepository: StandingsRepository
    private val standingsViewDataMapper = StandingsViewDataMapperImpl()
    private val testDispatcher = UnconfinedTestDispatcher(scheduler = TestCoroutineScheduler())

    @Mock
    lateinit var footballService: FootballService

    @Mock
    private lateinit var observer: Observer<StandingsViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        standingsRepository = StandingsRepositoryImpl(footballService, testDispatcher)
        standingsUseCase = StandingsUseCaseImpl(standingsRepository,standingsViewDataMapper)
        standingsViewModel = StandingsViewModel(standingsUseCase, testDispatcher)
    }

    @Test
    fun `When getting standings fails then return error view state`() = runTest{
        whenever(footballService.getStandings("3","2023")).thenReturn(Result.failure(Throwable("errorr")))
        val argumentCaptor = argumentCaptor<StandingsViewState>()
        standingsViewModel.standingsViewState.observeForever(observer)

        standingsViewModel.getStandings(leagueId = "3")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(this.capture())
            assertEquals(StandingsViewState.Initial, firstValue)
            assertEquals(StandingsViewState.Loading, secondValue)
            assertSame(StandingsViewState.Error, thirdValue)
        }
    }

    @Test
    fun `When football service fails then return error standings Result`() = runTest {
        whenever(footballService.getStandings("3","2023")).thenReturn(Result.failure(Throwable("Error")))

        val sut = standingsUseCase.getLeagueStandings("3")

        assertEquals(StandingsResult.Error, sut)
        verify(footballService).getStandings("3","2023")
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }
}