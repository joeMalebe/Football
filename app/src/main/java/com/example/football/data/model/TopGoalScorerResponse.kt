package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopScorersResponse(
    val errors: List<String> = emptyList(),
    val get: String = "",
    val paging: Paging = Paging(),
    val parameters: Parameters = Parameters(),
    val response: List<PlayerData> = emptyList(),
    val results: Int = 0
)

@Serializable
data class Paging(
    val current: Int = 0,
    val total: Int = 0
)

@Serializable
data class Parameters(
    val league: String = "",
    val season: String = ""
)

@Serializable
data class PlayerData(
    val player: Player = Player(),
    val statistics: List<Statistics> = emptyList()
)

@Serializable
data class Player(
    val age: Int = 0,
    val birth: Birth = Birth(),
    val firstname: String = "",
    val height: String = "",
    val id: Int = 0,
    val injured: Boolean = false,
    val lastname: String = "",
    val name: String = "",
    val nationality: String = "",
    val photo: String = "",
    val weight: String = ""
)

@Serializable
data class Birth(
    val country: String = "",
    val date: String = "",
    val place: String = ""
)

@Serializable
data class Statistics(
    val cards: CardStats = CardStats(),
    val dribbles: DribbleStats = DribbleStats(),
    val duels: DuelStats = DuelStats(),
    val fouls: FoulStats = FoulStats(),
    val games: GameStats = GameStats(),
    val goals: GoalStats = GoalStats(),
    val league: League,
    val passes: PassStats = PassStats(),
    val penalty: PenaltyStats = PenaltyStats(),
    val shots: ShotStats = ShotStats(),
    val substitutes: SubstituteStats = SubstituteStats(),
    val tackles: TackleStats = TackleStats(),
    val team: Team
)

@Serializable
data class CardStats(
    val red: Int = 0,
    val yellow: Int = 0,
    @SerialName("yellowred") val yellowRed: Int = 0
)

@Serializable
data class DribbleStats(
    val attempts: Int = 0,
    val past: Int? = null,
    val success: Int = 0
)

@Serializable
data class DuelStats(
    val total: Int = 0,
    val won: Int = 0
)

@Serializable
data class FoulStats(
    val committed: Int = 0,
    val drawn: Int = 0
)

@Serializable
data class GameStats(
    val appearences: Int = 0,
    val captain: Boolean = false,
    val lineups: Int = 0,
    val minutes: Int = 0,
    val number: Int? = null,
    val position: String = "",
    val rating: String = ""
)

@Serializable
data class GoalStats(
    val assists: Int = 0,
    val conceded: Int = 0,
    val saves: Int? = null,
    val total: Int = 0
)

@Serializable
data class PassStats(
    val accuracy: Int = 0,
    val key: Int = 0,
    val total: Int = 0
)

@Serializable
data class PenaltyStats(
    val commited: Int? = null,
    val missed: Int = 0,
    val saved: Int? = null,
    val scored: Int = 0,
    val won: Int? = null
)

@Serializable
data class ShotStats(
    val on: Int = 0,
    val total: Int = 0
)

@Serializable
data class SubstituteStats(
    val bench: Int = 0,
    @SerialName("in") val inCount: Int = 0,
    val out: Int = 0
)

@Serializable
data class TackleStats(
    val blocks: Int = 0,
    val interceptions: Int = 0,
    val total: Int = 0
)
