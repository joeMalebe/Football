package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class TopScorersResponse(
    val errors: List<String>? = null,
    val get: String? = null,
    val paging: Paging? = null,
    val parameters: Parameters? = null,
    val response: List<PlayerData>? = null,
    val results: Int? = null
)

@Serializable
data class Paging(
    val current: Int? = null,
    val total: Int? = null
)

@Serializable
data class Parameters(
    val league: String? = null,
    val season: String? = null
)

@Serializable
data class PlayerData(
    val player: Player? = null,
    val statistics: List<Statistics>? = null
)

@Serializable
data class Player(
    val age: Int? = null,
    val birth: Birth? = null,
    val firstname: String? = null,
    val height: String? = null,
    val id: Int? = null,
    val injured: Boolean? = null,
    val lastname: String? = null,
    val name: String? = null,
    val nationality: String? = null,
    val photo: String? = null,
    val weight: String? = null
)

@Serializable
data class Birth(
    val country: String? = null,
    val date: String? = null,
    val place: String? = null
)

@Serializable
data class Statistics(
    val cards: CardStats? = null,
    val dribbles: DribbleStats? = null,
    val duels: DuelStats? = null,
    val fouls: FoulStats? = null,
    val games: GameStats? = null,
    val goals: GoalStats? = null,
    val league: League? = null,
    val passes: PassStats? = null,
    val penalty: PenaltyStats? = null,
    val shots: ShotStats? = null,
    val substitutes: SubstituteStats? = null,
    val tackles: TackleStats? = null,
    val team: Team? = null
)

@Serializable
data class CardStats(
    val red: Int? = null,
    val yellow: Int? = null,
    @SerialName("yellowred") val yellowRed: Int? = null
)

@Serializable
data class DribbleStats(
    val attempts: Int? = null,
    val past: Int? = null,
    val success: Int? = null
)

@Serializable
data class DuelStats(
    val total: Int? = null,
    val won: Int? = null
)

@Serializable
data class FoulStats(
    val committed: Int? = null,
    val drawn: Int? = null
)

@Serializable
data class GameStats(
    val appearences: Int? = null,
    val captain: Boolean? = null,
    val lineups: Int? = null,
    val minutes: Int? = null,
    val number: Int? = null,
    val position: String? = null,
    val rating: String? = null
)

@Serializable
data class GoalStats(
    val assists: Int? = null,
    val conceded: Int? = null,
    val saves: Int? = null,
    val total: Int? = null
)

@Serializable
data class PassStats(
    val accuracy: Int? = null,
    val key: Int? = null,
    val total: Int? = null
)

@Serializable
data class PenaltyStats(
    val commited: Int? = null,
    val missed: Int? = null,
    val saved: Int? = null,
    val scored: Int? = null,
    val won: Int? = null
)

@Serializable
data class ShotStats(
    val on: Int? = null,
    val total: Int? = null
)

@Serializable
data class SubstituteStats(
    val bench: Int? = null,
    @SerialName("in") val inCount: Int? = null,
    val out: Int? = null
)

@Serializable
data class TackleStats(
    val blocks: Int? = null,
    val interceptions: Int? = null,
    val total: Int? = null
)