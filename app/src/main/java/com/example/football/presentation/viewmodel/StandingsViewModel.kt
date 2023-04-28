package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.StandingsResult
import com.example.football.domain.usecase.StandingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class StandingsViewModel @Inject constructor(
    private val standingsUseCase: StandingsUseCase,
    val ioContext: CoroutineContext
) : ViewModel() {
    private val _standingsViewState =
        MutableLiveData<StandingsViewState>(StandingsViewState.Initial)
    val standingsViewState: LiveData<StandingsViewState>
        get() = _standingsViewState

    fun getStandings(leagueId: String) {
        viewModelScope.launch(ioContext) {
            _standingsViewState.postValue(StandingsViewState.Loading)

           val result = standingsUseCase.getLeagueStandings(leagueId)

           when(result) {
              is StandingsResult.Error -> {
                   _standingsViewState.postValue(StandingsViewState.Error)
               }
               else -> {
                   _standingsViewState.postValue(StandingsViewState.Initial)
               }
           }
        }
    }
}