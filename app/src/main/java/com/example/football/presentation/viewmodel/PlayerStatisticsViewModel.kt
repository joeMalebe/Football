package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.usecase.PlayerStatisticsUseCase
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PlayerStatisticsViewModel(
    private val playerStatisticsUseCase: PlayerStatisticsUseCase,
    private val ioContext: CoroutineContext
):ViewModel() {

    private val playerStatisticsViewStateLiveData = MutableLiveData<PlayerStatisticsViewState>()
    val playerStatisticsViewState: LiveData<PlayerStatisticsViewState>
        get() = playerStatisticsViewStateLiveData

    fun getPlayerStatistics(playerId: Int, season: String) {
        playerStatisticsViewStateLiveData.value = PlayerStatisticsViewState.Loading
        viewModelScope.launch(ioContext) {
            val playerStatisticsViewData = playerStatisticsUseCase.getPlayerStatistics(
                playerId,
                season
            )
            playerStatisticsViewStateLiveData.postValue(
                PlayerStatisticsViewState.Success(
                    playerStatisticsViewData
                )
            )
        }
    }
}

sealed class PlayerStatisticsViewState {
    data class Success(val playerStatisticsViewData: PlayerStatisticsViewData) :
        PlayerStatisticsViewState()

    object Loading : PlayerStatisticsViewState()
}