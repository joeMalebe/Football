package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class League(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String? = "",
    @SerialName("country") val country: String? = "",
    @SerialName("logo") val logo: String? = "",
    @SerialName("flag") val flag: String? = "",
    @SerialName("season") val season: Int,
    @SerialName("standings") val standings: List<List<TeamStandingDto>> = emptyList()
)

@Serializable
data class LeagueInfo(
    @SerialName("league") val league: League
)

@Serializable
data class TeamStandingDto(
    @SerialName("rank") val rank: Int,
    @SerialName("team") val team: Team,
    @SerialName("points") val points: Int,
    @SerialName("goalsDiff") val goalsDiff: Int,
    @SerialName("group") val group: String? = "",
    @SerialName("form") val form: String? = "",
    @SerialName("status") val status: String? = "",
    @SerialName("description") val description: String? = "",
    @SerialName("all") val all: TeamStatsDto,
    @SerialName("home") val home: TeamStatsDto,
    @SerialName("away") val away: TeamStatsDto,
    @SerialName("update") val update: String? = ""
)

@Serializable
data class Team(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String? = "",
    @SerialName("logo") val logo: String? = ""
)

@Serializable
data class TeamStatsDto(
    @SerialName("played") val played: Int,
    @SerialName("win") val win: Int,
    @SerialName("draw") val draw: Int,
    @SerialName("lose") val lose: Int,
    @SerialName("goals") val goals: TeamGoalsDto
)

@Serializable
data class TeamGoalsDto(
    @SerialName("for") val `for`: Int,
    @SerialName("against") val against: Int
)

@Serializable
data class StandingsResponse(
    @SerialName("response") val response: List<LeagueInfo>
)
