package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerStatisticsDto(
    @SerialName("response")
    val response: List<ResponseDto> = listOf()
)

@Serializable
data class ResponseDto(
    @SerialName("player")
    val player: PlayerDto = PlayerDto(),
    @SerialName("statistics")
    val statistics: List<StatisticDto> = listOf()
)

@Serializable
data class PlayerDto(
    @SerialName("age")
    val age: Int = 0,
    @SerialName("birth")
    val birth: BirthDto = BirthDto(),
    @SerialName("firstname")
    val firstname: String = "",
    @SerialName("height")
    val height: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("injured")
    val injured: Boolean = false,
    @SerialName("lastname")
    val lastname: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("nationality")
    val nationality: String = "",
    @SerialName("photo")
    val photo: String = "",
    @SerialName("weight")
    val weight: String = ""
)

@Serializable
data class StatisticDto(
    @SerialName("cards")
    val cards: CardsDto = CardsDto(),
    @SerialName("dribbles")
    val dribbles: DribblesDto = DribblesDto(),
    @SerialName("duels")
    val duels: DuelsDto = DuelsDto(),
    @SerialName("fouls")
    val fouls: FoulsDto = FoulsDto(),
    @SerialName("games")
    val games: GamesDto = GamesDto(),
    @SerialName("goals")
    val goals: GoalsDto = GoalsDto(),
    @SerialName("league")
    val league: LeagueDto = LeagueDto(),
    @SerialName("passes")
    val passes: PassesDto = PassesDto(),
    @SerialName("penalty")
    val penalty: PenaltyDto = PenaltyDto(),
    @SerialName("shots")
    val shots: ShotsDto = ShotsDto(),
    @SerialName("substitutes")
    val substitutes: SubstitutesDto = SubstitutesDto(),
    @SerialName("tackles")
    val tackles: TacklesDto = TacklesDto(),
    @SerialName("team")
    val team: TeamDto = TeamDto()
)

@Serializable
data class BirthDto(
    @SerialName("country")
    val country: String = "",
    @SerialName("date")
    val date: String = "",
    @SerialName("place")
    val place: String = ""
)

@Serializable
data class CardsDto(
    @SerialName("red")
    val red: Int = 0,
    @SerialName("yellow")
    val yellow: Int = 0,
    @SerialName("yellowred")
    val yellowred: Int = 0
)

@Serializable
data class DribblesDto(
    @SerialName("attempts")
    val attempts: Int? = 0,
    @SerialName("success")
    val success: Int? = 0
)

@Serializable
data class DuelsDto(
    @SerialName("total")
    val total: Int? = 0,
    @SerialName("won")
    val won: Int? = 0
)

@Serializable
data class FoulsDto(
    @SerialName("committed")
    val committed: Int? = 0,
    @SerialName("drawn")
    val drawn: Int? = 0
)

@Serializable
data class GamesDto(
    @SerialName("appearences")
    val appearences: Int = 0,
    @SerialName("captain")
    val captain: Boolean = false,
    @SerialName("lineups")
    val lineups: Int = 0,
    @SerialName("minutes")
    val minutes: Int = 0,
    @SerialName("position")
    val position: String = "",
    @SerialName("rating")
    val rating: String? = ""
)

@Serializable
data class GoalsDto(
    @SerialName("assists")
    val assists: Int? = 0,
    @SerialName("conceded")
    val conceded: Int? = 0,
    @SerialName("saves")
    val saves: Int? = null,
    @SerialName("total")
    val total: Int = 0
)

@Serializable
data class PassesDto(
    @SerialName("accuracy")
    val accuracy: Int? = 0,
    @SerialName("key")
    val key: Int? = 0,
    @SerialName("total")
    val total: Int? = 0
)

@Serializable
data class PenaltyDto(
    @SerialName("commited")
    val commited: Int? = null,
    @SerialName("missed")
    val missed: Int? = 0,
    @SerialName("saved")
    val saved: Int? = null,
    @SerialName("scored")
    val scored: Int? = 0,
    @SerialName("won")
    val won: Int? = null
)

@Serializable
data class ShotsDto(
    @SerialName("on")
    val on: Int? = 0,
    @SerialName("total")
    val total: Int? = 0
)

@Serializable
data class SubstitutesDto(
    @SerialName("bench")
    val bench: Int = 0,
    @SerialName("in")
    val inX: Int = 0,
    @SerialName("out")
    val `out`: Int = 0
)

@Serializable
data class TacklesDto(
    @SerialName("blocks")
    val blocks: Int? = 0,
    @SerialName("interceptions")
    val interceptions: Int? = 0,
    @SerialName("total")
    val total: Int? = 0
)

@Serializable
data class TeamDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("logo")
    val logo: String = "",
    @SerialName("name")
    val name: String = ""
)