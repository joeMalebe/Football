package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StandingsCombinedViewModel : ViewModel() {

    private val _standingsCombinedViewState =
        MutableLiveData(StandingsCombinedViewState(combinedViewState = true))
    val standingsCombinedViewState: LiveData<StandingsCombinedViewState>
        get() = _standingsCombinedViewState

    fun onTopGoalScorerSeeAllClicked() {
        _standingsCombinedViewState.postValue(
            _standingsCombinedViewState.value?.let {
                it.setState(
                    topGoalScorerSeeAll = !it.topGoalScorerSeeAll,
                    combinedViewState = it.topGoalScorerSeeAll
                )
            }
        )
    }

    fun onStandingsSeeAllClicked() {
        _standingsCombinedViewState.postValue(
            _standingsCombinedViewState.value?.let {
                it.setState(
                    standingsSeeAll = !it.standingsSeeAll,
                    combinedViewState = it.standingsSeeAll
                )
            }
        )
    }

    fun onLoadingComplete() {
        _standingsCombinedViewState.postValue(
            _standingsCombinedViewState.value?.setState(
                combinedViewState = true
            )
        )
    }
}

data class StandingsCombinedViewState(
    val loading: Boolean = false,
    val combinedViewState: Boolean = false,
    val topGoalScorerSeeAll: Boolean = false,
    val standingsSeeAll: Boolean = false,
    val error: Boolean = false
) {
    fun setState(
        loading: Boolean = false,
        combinedViewState: Boolean = false,
        topGoalScorerSeeAll: Boolean = false,
        standingsSeeAll: Boolean = false,
        error: Boolean = false
    ) =
        copy(
            loading = loading,
            combinedViewState = combinedViewState,
            topGoalScorerSeeAll = topGoalScorerSeeAll,
            standingsSeeAll = standingsSeeAll,
            error = error
        )
}
