package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    @SerialName("fixture")
    val fixture: FixtureDto = FixtureDto(),
    @SerialName("goals")
    val goals: GoalsDto = GoalsDto(),
    @SerialName("league")
    val league: LeagueDto = LeagueDto(),
    @SerialName("score")
    val score: ScoreDto = ScoreDto(),
    @SerialName("teams")
    val teams: TeamsDto = TeamsDto()
)