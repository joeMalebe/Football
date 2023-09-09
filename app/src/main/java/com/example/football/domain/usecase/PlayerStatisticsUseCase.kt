package com.example.football.domain.usecase

import com.example.football.data.model.PlayerDto
import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.model.StatisticDto
import com.example.football.data.repository.PlayerStatisticsRepository
import com.example.football.domain.PlayerInfoViewData
import com.example.football.domain.PlayerStatisticsViewData
import com.example.football.domain.StatisticsViewDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal

const val PERCENTAGE_MULTIPLIER = 10

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
    PlayerStatisticsUseCase {
    private fun mapPlayerStatsDtoToViewData(dto: PlayerStatisticsDto): PlayerStatisticsViewData {
        val playerDto = dto.response.firstOrNull()!!
        val playerRating =
            getMaxPlayerRatingfromDifferentCompetiitons(playerDto.statistics)
        val playerInfo = mapPlayerInfoDtoToViewData(
            playerDto.player,
            playerRating
        )
        val statisticsViewDate = mapStatisticsDtoToViewData(playerDto.statistics)
        return PlayerStatisticsViewData(playerInfo, statisticsViewDate)
    }

    private fun getMaxPlayerRatingfromDifferentCompetiitons(statistics: List<StatisticDto>) =
        statistics.maxOf { it.games.rating?.toBigDecimalOrNull() ?: 0.toBigDecimal() }

    private fun mapStatisticsDtoToViewData(dto: List<StatisticDto>): ImmutableList<StatisticsViewDate> {
        return dto.map {
            StatisticsViewDate(
                games = it.games.appearences,
                shots = it.shots.total ?: 0,
                assists = it.goals.assists ?: 0,
                goals = it.goals.total,
                tackles = it.tackles.total ?: 0,
                dribblesCompleted = it.dribbles.success ?: 0,
                fouls = it.fouls.committed ?: 0,
                redCards = it.cards.red,
                yellowCards = it.cards.yellow,
                duelsWon = it.duels.won ?: 0,
                team = it.team.name,
                teamLogoUrl = it.team.logo,
                competition = it.league.name,
                competitionImageUrl = it.league.logo,
                passes = it.passes.total ?: 0
            )
        }.toImmutableList()
    }

    private fun mapPlayerInfoDtoToViewData(
        player: PlayerDto,
        playerRating: BigDecimal
    ): PlayerInfoViewData {
        return PlayerInfoViewData(
            fullName = concatFirstAndLastName(player),
            surname = player.lastname,
            age = player.age,
            weight = player.weight,
            imageUrl = player.photo,
            playerRating = convertPlayerRatingToPercentageRoundNumber(playerRating)
        )
    }

    private fun concatFirstAndLastName(player: PlayerDto) =
        "${player.name} ${player.lastname}"

    private fun convertPlayerRatingToPercentageRoundNumber(playerRating: BigDecimal) =
        playerRating.multiply(BigDecimal(PERCENTAGE_MULTIPLIER)).toInt()

    override suspend fun getPlayerStatistics(
        playerId: String,
        season: String
    ): PlayerStatisticsResult {
        val result = playerStatisticsRepository.getPlayerStatistics(playerId, season)
        return PlayerStatisticsResult.PlayerStats(mapPlayerStatsDtoToViewData(result.getOrNull()!!))
    }

}


