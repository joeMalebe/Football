package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.usecase.PlayerStatisticsResult
import com.example.football.domain.usecase.PlayerStatisticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerStatisticsViewModel @Inject constructor(
    private val playerStatisticsUseCase: PlayerStatisticsUseCase,
    private val ioContext: CoroutineContext
) : ViewModel() {

    private val _playerStatisticsViewState = MutableLiveData<PlayerStatisticsViewState>()
    val playerStatisticsViewState: LiveData<PlayerStatisticsViewState>
        get() = _playerStatisticsViewState

    fun getPlayerStatistics(playerId: String, season: String) {
        _playerStatisticsViewState.postValue(PlayerStatisticsViewState.Loading)
        viewModelScope.launch(ioContext) {
            when (val result = playerStatisticsUseCase.getPlayerStatistics(playerId, season)) {
                is PlayerStatisticsResult.Error -> _playerStatisticsViewState.postValue(
                    PlayerStatisticsViewState.Error
                )

                is PlayerStatisticsResult.NoInformation -> _playerStatisticsViewState.postValue(
                    PlayerStatisticsViewState.NoInformation
                )

                is PlayerStatisticsResult.PlayerStats -> _playerStatisticsViewState.postValue(
                    PlayerStatisticsViewState.Success(
                        result.playerStatisticsViewData
                    )
                )
            }
        }
    }
}

sealed class PlayerStatisticsViewState {
    data class Success(val playerStatisticsViewData: PlayerStatisticsViewData) :
        PlayerStatisticsViewState()

    object Loading : PlayerStatisticsViewState()

    object Error : PlayerStatisticsViewState()

    object NoInformation : PlayerStatisticsViewState()
}