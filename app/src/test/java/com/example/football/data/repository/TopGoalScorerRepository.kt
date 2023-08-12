package com.example.football.data.repository

import com.example.football.FootballService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.whenever


@OptIn(ExperimentalCoroutinesApi::class)
class TopGoalScorerRepositoryTest {

    @Mock
    lateinit var footballService: FootballService
    @OptIn(ExperimentalCoroutinesApi::class)
    val testDispatcher = UnconfinedTestDispatcher(scheduler = TestCoroutineScheduler())
    lateinit var repository : TopGoalScorerRepository
    @Before
    fun setUp() {
        openMocks(this)
        repository =  TopGoalScorerRepositoryImpl(footballService, testDispatcher)
    }

    @Test
    fun getTopGoalScorer() = runTest {
        whenever(footballService.getTopScorers("1", "2020")).thenReturn(
            Result.success(TestData.topGoalScorerResponse)
        )
        val response = repository.getTopGoalScorer("1", "2020")
        assertEquals(response, Result.success(TestData.topGoalScorerResponse))
    }
}

