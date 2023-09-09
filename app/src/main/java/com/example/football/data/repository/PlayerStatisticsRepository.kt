package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.PlayerStatisticsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface PlayerStatisticsRepository {
    suspend fun getPlayerStatistics(playerId: String, season: String): Result<PlayerStatisticsDto>

}

internal class PlayerStatisticsRepositoryImpl(
    private val footballService: FootballService,
    private val ioContext: CoroutineContext
) : PlayerStatisticsRepository {
    override suspend fun getPlayerStatistics(
        playerId: String,
        season: String
    ): Result<PlayerStatisticsDto> {


        return withContext(CoroutineScope(ioContext).coroutineContext) {
            val response = footballService.getPlayer(playerId, season)
            Result.success(response.getOrNull()!!)
        }

    }
}
