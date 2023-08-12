package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.TopScorersResponse
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

interface TopGoalScorerRepository {
    suspend fun getTopGoalScorer(leagueId: String, season: String): Result<TopScorersResponse>
}

internal class TopGoalScorerRepositoryImpl(
    private val footballService: FootballService,
    private val ioContext: CoroutineContext
) : TopGoalScorerRepository {
    override suspend fun getTopGoalScorer(
        leagueId: String,
        season: String
    ): Result<TopScorersResponse> {
        return withContext(ioContext) {
            try {
                footballService.getTopScorers(leagueId, season)
            } catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
    }
}