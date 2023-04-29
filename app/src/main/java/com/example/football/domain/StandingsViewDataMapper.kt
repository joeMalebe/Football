package com.example.football.domain

import com.example.football.data.model.StandingsResponse

interface StandingsViewDataMapper {
    fun mapDtoToStandingsResult(standingsResponse: StandingsResponse?): StandingsResult
}

sealed class StandingsResult {
    object Error : StandingsResult()

    object NoStandingsInformation : StandingsResult()

    data class StandingsLoaded(val standingsViewData: List<StandingsViewData>) : StandingsResult()
}

class StandingsViewDataMapperImpl : StandingsViewDataMapper {
    override fun mapDtoToStandingsResult(standingsResponse: StandingsResponse?): StandingsResult {
        return if (standingsResponse == null || standingsResponse.response.isEmpty()) {
            StandingsResult.NoStandingsInformation
        } else {
            StandingsResult.StandingsLoaded(
                standingsResponse.response[0].league.standings[0].map {
                    StandingsViewData(
                        teamName = it.team.name,
                        teamId = it.team.id,
                        draws = it.all.draw,
                        losses = it.all.lose,
                        logo = it.team.logo,
                        points = it.points,
                        position = it.rank,
                        positionDescription = it.description,
                        wins = it.all.win
                    )
                }
            )
        }
    }
}