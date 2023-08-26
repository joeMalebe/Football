package com.example.football.presentation.viewmodel.viewstate

import com.example.football.domain.StandingsViewData

sealed class LeagueTableViewState {
    object Initial : LeagueTableViewState()
    object Loading : LeagueTableViewState()
    object Error : LeagueTableViewState()
    object NoInformation : LeagueTableViewState()
    data class StandingsLoaded(val standings: List<StandingsViewData>) : LeagueTableViewState()
}
