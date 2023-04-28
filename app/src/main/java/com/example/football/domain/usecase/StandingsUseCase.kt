package com.example.football.domain.usecase

import com.example.football.data.repository.StandingsRepository
import com.example.football.domain.StandingsResult
import com.example.football.domain.StandingsViewDataMapper
import java.util.*
import java.util.Calendar.YEAR

interface StandingsUseCase {
    suspend fun getLeagueStandings(leagueId: String): StandingsResult
}

class StandingsUseCaseImpl(
    val repository: StandingsRepository,
    private val standingsViewDataMapper: StandingsViewDataMapper
) : StandingsUseCase {
    override suspend fun getLeagueStandings(leagueId: String): StandingsResult {
        val response = repository.getStandings(
            leagueId = leagueId,
            season = Calendar.getInstance().get(YEAR).toString()
        )
        return if (response.isFailure) {
            StandingsResult.Error
        } else {
            standingsViewDataMapper.mapDtoToStandingsResult(response.getOrNull())
        }
    }
}