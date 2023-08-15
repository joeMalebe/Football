package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.football.domain.usecase.GetTopGoalScorersUseCase
import com.example.football.domain.usecase.TopGoalScorersResult
import com.example.football.presentation.viewmodel.viewstate.TopGoalScorersViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

@HiltViewModel
class TopGoalScorersViewModel @Inject constructor(
    private val useCase: GetTopGoalScorersUseCase,
    val ioContext: CoroutineContext
) : ViewModel() {
    private val _topGoalScorerViewState: MutableLiveData<TopGoalScorersViewState> =
        MutableLiveData(TopGoalScorersViewState.Initial)
    val topGoalScorerViewState: LiveData<TopGoalScorersViewState>
        get() = _topGoalScorerViewState

    suspend fun loadTopGoalScorers(leagueId: String, season: String) {
        _topGoalScorerViewState.postValue(TopGoalScorersViewState.Loading)
        withContext(ioContext) {
            useCase.getTopGoalScorers(leagueId = leagueId, season = season).let { result ->
                when (result) {
                    is TopGoalScorersResult.Loaded -> {
                        _topGoalScorerViewState.postValue(
                            TopGoalScorersViewState.TopGoalScorersLoaded(
                                result.topGoalScorersViewData
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
}