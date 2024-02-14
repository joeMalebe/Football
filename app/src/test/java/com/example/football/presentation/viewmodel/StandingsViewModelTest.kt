package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.FootballService
import com.example.football.data.model.StandingsResponse
import com.example.football.data.repository.StandingsRepository
import com.example.football.data.repository.StandingsRepositoryImpl
import com.example.football.data.repository.TestData.standingsResponse
import com.example.football.domain.StandingsResult
import com.example.football.domain.StandingsViewDataMapperImpl
import com.example.football.domain.usecase.StandingsUseCase
import com.example.football.domain.usecase.StandingsUseCaseImpl
import com.example.football.presentation.viewmodel.viewstate.LeagueTableViewState
import java.util.Calendar
import java.util.Calendar.YEAR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
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
    private val year = Calendar.getInstance().get(YEAR).toString()

    @Mock
    lateinit var footballService: FootballService

    @Mock
    private lateinit var observer: Observer<LeagueTableViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        standingsRepository = StandingsRepositoryImpl(footballService, testDispatcher)
        standingsUseCase = StandingsUseCaseImpl(standingsRepository, standingsViewDataMapper)
        standingsViewModel = StandingsViewModel(standingsUseCase, testDispatcher)
    }

    @Test
    fun `When getting standings fails then return error view state`() = runTest {
        whenever(footballService.getStandings("3", year)).thenReturn(
            Result.failure(Throwable("errorr"))
        )
        val argumentCaptor = argumentCaptor<LeagueTableViewState>()
        standingsViewModel.standingsViewState.observeForever(observer)

        standingsViewModel.getStandings(leagueId = "3")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(this.capture())
            assertEquals(LeagueTableViewState.Initial, firstValue)
            assertEquals(LeagueTableViewState.Loading, secondValue)
            assertSame(LeagueTableViewState.Error, thirdValue)
        }
    }

    @Test
    fun `When football service fails then return error standings Result`() = runTest {
        whenever(footballService.getStandings("3", year)).thenReturn(
            Result.failure(Throwable("Error"))
        )

        val sut = standingsUseCase.getLeagueStandings("3")

        assertEquals(StandingsResult.Error, sut)
        verify(footballService).getStandings("3", year)
    }

    @Test
    fun `When football service throws an exception then return error standings result`() = runTest {
        whenever(footballService.getStandings("3", year)).thenThrow(
            java.lang.RuntimeException("error")
        )

        val sut = standingsUseCase.getLeagueStandings("3")

        assertSame(StandingsResult.Error, sut)
    }

    @Test
    fun `When get standings is successful then emit standings loaded state`() = runTest {
        whenever(footballService.getStandings("5", year)).thenReturn(
            Result.success(
                standingsResponse
            )
        )
        val argumentCaptor = argumentCaptor<LeagueTableViewState>()
        standingsViewModel.standingsViewState.observeForever(observer)

        standingsViewModel.getStandings("5")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(this.capture())
            assertSame(LeagueTableViewState.Initial, firstValue)
            assertSame(LeagueTableViewState.Loading, secondValue)
            assertSame(LeagueTableViewState.StandingsLoaded::class.java, thirdValue::class.java)
        }
    }

    @Test
    fun `When get standings has no standings data then emit no information state`() = runTest {
        whenever(footballService.getStandings("12", year)).thenReturn(
            Result.success(
                StandingsResponse(emptyList())
            )
        )
        val argumentCaptor = argumentCaptor<LeagueTableViewState>()
        standingsViewModel.standingsViewState.observeForever(observer)

        standingsViewModel.getStandings("12")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(this.capture())
            assertSame(LeagueTableViewState.Initial, firstValue)
            assertSame(LeagueTableViewState.Loading, secondValue)
            assertSame(LeagueTableViewState.NoInformation, thirdValue)
        }
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }
}
