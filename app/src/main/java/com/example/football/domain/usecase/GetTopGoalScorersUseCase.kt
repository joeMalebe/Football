package com.example.football.domain.usecase

import com.example.football.data.model.TopScorersResponse
import com.example.football.data.repository.TopGoalScorerRepository
import com.example.football.domain.TopGoalScorerViewData

interface GetTopGoalScorersUseCase {
    suspend fun getTopGoalScorers(leagueId: String, season: String): TopGoalScorersResult
}

internal class GetTopGoalScorersUseCaseImpl(
    private val topGoalScorerRepository: TopGoalScorerRepository
) :
    GetTopGoalScorersUseCase {
    override suspend fun getTopGoalScorers(leagueId: String, season: String): TopGoalScorersResult {
        val result = topGoalScorerRepository.getTopGoalScorer(leagueId, season)
        return if (result.isSuccess) {
            val topGoalScorers = mapDtoToViewData(result)
            if (topGoalScorers.isNotEmpty()) {
                TopGoalScorersResult.Loaded(topGoalScorers)
            } else {
                TopGoalScorersResult.NoTopGoalScorersInformation
            }
        } else {
            TopGoalScorersResult.Error
        }
    }

    private fun mapDtoToViewData(result: Result<TopScorersResponse>) =
        result.getOrNull()?.response?.map { playerData ->
            TopGoalScorerViewData(
                playerId = playerData.player?.id ?: 0,
                playerName = playerData.player?.firstname ?: "",
                playerSurname = playerData.player?.lastname ?: "",
                numberOfGoals = playerData.statistics?.firstOrNull()?.goals?.total ?: 0,
                playerImgUrl = playerData.player?.photo ?: ""
            )
        } ?: emptyList()
}

sealed class TopGoalScorersResult {
    data class Loaded(val topGoalScorersViewData: List<TopGoalScorerViewData>) :
        TopGoalScorersResult()

    object Error : TopGoalScorersResult()
    object NoTopGoalScorersInformation : TopGoalScorersResult()
}
