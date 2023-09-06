package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.PlayerStatisticsDto

interface PlayerStatisticsRepository {
    suspend fun getPlayerStatistics(playerId: String, season: String): Result<PlayerStatisticsDto>

}

internal class PlayerStatisticsRepositoryImpl(
    private val footballService: FootballService
) : PlayerStatisticsRepository {
    override suspend fun getPlayerStatistics(
        playerId: String,
        season: String
    ): Result<PlayerStatisticsDto> {
        val response = footballService.getPlayer(playerId, season)
        if (response.isSuccess) {
            response.getOrNull()

        } else {
            Result.failure<PlayerStatisticsDto>(Exception("Error getting player statistics"))
        }
    }

}
