package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.TopGoalScorerViewData
import com.example.football.domain.usecase.GetTopGoalScorersUseCase
import com.example.football.domain.usecase.TopGoalScorersResult
import com.example.football.presentation.viewmodel.viewstate.TopGoalScorersViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

@HiltViewModel
class TopGoalScorersViewModel @Inject constructor(
    private val useCase: GetTopGoalScorersUseCase,
    val ioContext: CoroutineContext
) : ViewModel() {
    private var isSeeAll = false
    private var topGoalScorers: List<TopGoalScorerViewData> = emptyList()
    private val _topGoalScorerViewState: MutableLiveData<TopGoalScorersViewState> =
        MutableLiveData(TopGoalScorersViewState.Initial)
    val topGoalScorerViewState: LiveData<TopGoalScorersViewState>
        get() = _topGoalScorerViewState

    fun loadTopGoalScorers(leagueId: String, season: String) {
        _topGoalScorerViewState.postValue(TopGoalScorersViewState.Loading)
        viewModelScope.launch(ioContext) {
            useCase.getTopGoalScorers(leagueId = leagueId, season = season).let { result ->
                when (result) {
                    is TopGoalScorersResult.Loaded -> {
                        topGoalScorers = result.topGoalScorersViewData
                        _topGoalScorerViewState.postValue(
                            TopGoalScorersViewState.TopGoalScorersLoaded(
                                getTopGoalScorers(topGoalScorers, isSeeAll)
                            )
                        )
                    }

                    is TopGoalScorersResult.NoTopGoalScorersInformation -> {
                        _topGoalScorerViewState.postValue(TopGoalScorersViewState.NoInformation)
                    }

                    else -> {
                        _topGoalScorerViewState.postValue(TopGoalScorersViewState.Error)
                    }
                }
            }
        }
    }

    fun getTopGoalScorers(topGoalScorers: List<TopGoalScorerViewData>, isSeeAll: Boolean) =
        if (isSeeAll) {
            topGoalScorers
        } else {
            topGoalScorers.take(5)
        }

    fun onSeeAllClick() {
        toggleSeeAllResults()
    }

    private fun toggleSeeAllResults() {
        isSeeAll = !isSeeAll
        _topGoalScorerViewState.postValue(
            TopGoalScorersViewState.TopGoalScorersLoaded(
                getTopGoalScorers(topGoalScorers, isSeeAll)
            )
        )
    }
}