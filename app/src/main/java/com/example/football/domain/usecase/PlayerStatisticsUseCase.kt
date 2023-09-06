package com.example.football.domain.usecase

import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.repository.PlayerStatisticsRepository
import com.example.football.domain.PlayerStatisticsViewData

interface PlayerStatisticsUseCase {
     suspend fun getPlayerStatistics(playerId: String, season: String): PlayerStatisticsResult

}

sealed class PlayerStatisticsResult {
    data class PlayerStats(val playerStatisticsViewData: PlayerStatisticsViewData) :
        PlayerStatisticsResult()

    object Error : PlayerStatisticsResult()
    object NoInformation : PlayerStatisticsResult()
}

internal class PlayerStatisticsUseCaseImpl(private val playerStatisticsRepository: PlayerStatisticsRepository) :
    PlayerStatisticsUseCase{
    private fun maPlayerStatspDtoToViewData(orNull: PlayerStatisticsDto?) {

        val playerInfo = mapPlayerInfoDtoToViewData(or)

    /*val player = PlayerStatisticsViewData(

        )*/
    }

    override suspend fun getPlayerStatistics(playerId: String, season: String): PlayerStatisticsResult {
        val result = playerStatisticsRepository.getPlayerStatistics(playerId, season)

        if (result.isSuccess) {
            maPlayerStatspDtoToViewData(result.getOrNull()
        }
        return when (val playerStatistics = playerStatisticsRepository.getPlayerStatistics(playerId, season)) {
            is PlayerStatisticsResult.PlayerStats -> {
                PlayerStatisticsResult.PlayerStats(playerStatistics.playerStatisticsViewData)
            }
            is PlayerStatisticsResult.Error -> {
                PlayerStatisticsResult.Error
            }
            is PlayerStatisticsResult.NoInformation -> {
                PlayerStatisticsResult.NoInformation
            }
        }
    }

}


