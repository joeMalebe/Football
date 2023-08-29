package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class StandingsCombinedViewModelTest {

    val standingsCombinedViewModel = StandingsCombinedViewModel()

    @Mock
    lateinit var observer: Observer<StandingsCombinedViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        openMocks(this)
    }

    @Test
    fun `When onTopGoalScorerSeeAllClicked is called then emit opposite values for topGoalScorerSeeAll and combinedViewState`() {
        standingsCombinedViewModel.standingsCombinedViewState.observeForever(observer)

        standingsCombinedViewModel.onTopGoalScorerSeeAllClicked()
        standingsCombinedViewModel.onTopGoalScorerSeeAllClicked()

        verify(observer).onChanged(
            StandingsCombinedViewState(
                topGoalScorerSeeAll = true,
                combinedViewState = false
            )
        )
        verify(observer, times(2)).onChanged(
            StandingsCombinedViewState(
                topGoalScorerSeeAll = false,
                combinedViewState = true
            )
        )
        verify(observer, times(0)).onChanged(
            StandingsCombinedViewState(
                topGoalScorerSeeAll = false,
                combinedViewState = false
            )
        )
        verify(observer, times(0)).onChanged(
            StandingsCombinedViewState(
                topGoalScorerSeeAll = true,
                combinedViewState = true
            )
        )
    }

    @Test
    fun `When onStandingsSeeAllClicked is called then emit standingsSeeAll true`() {
        standingsCombinedViewModel.standingsCombinedViewState.observeForever(observer)

        standingsCombinedViewModel.onStandingsSeeAllClicked()
        standingsCombinedViewModel.onStandingsSeeAllClicked()

        verify(observer).onChanged(
            StandingsCombinedViewState(
                standingsSeeAll = true,
                combinedViewState = false
            )
        )

        verify(observer, times(2)).onChanged(
            StandingsCombinedViewState(
                standingsSeeAll = false,
                combinedViewState = true
            )
        )
        verify(observer, times(0)).onChanged(
            StandingsCombinedViewState(
                standingsSeeAll = false,
                combinedViewState = false
            )
        )
        verify(observer, times(0)).onChanged(
            StandingsCombinedViewState(
                standingsSeeAll = true,
                combinedViewState = true
            )
        )
    }

    @Test
    fun `When onLoadingComplete is called then emit combinedViewState true`() {
        standingsCombinedViewModel.standingsCombinedViewState.observeForever(observer)

        standingsCombinedViewModel.onLoadingComplete()

        verify(observer, times(2)).onChanged(StandingsCombinedViewState(combinedViewState = true))
    }
}