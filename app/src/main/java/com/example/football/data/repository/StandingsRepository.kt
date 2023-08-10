package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.StandingsResponse
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

interface StandingsRepository {
    suspend fun getStandings(leagueId: String, season: String): Result<StandingsResponse>
}

internal class StandingsRepositoryImpl(
    private val footballService: FootballService,
    private val ioContext: CoroutineContext
) : StandingsRepository {
    override suspend fun getStandings(leagueId: String, season: String): Result<StandingsResponse> {
        return withContext(ioContext) {
            try {
                footballService.getStandings(leagueId, season)
            } catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
    }
}
