package com.example.football.presentation.viewmodel.viewstate

import com.example.football.domain.StandingsViewData

sealed class StandingsViewState() {
    object Initial : StandingsViewState()
    object Loading : StandingsViewState()
    object Error : StandingsViewState()
    object NoInformation : StandingsViewState()
    data class StandingsLoaded(val standings: List<StandingsViewData>) : StandingsViewState()
}
