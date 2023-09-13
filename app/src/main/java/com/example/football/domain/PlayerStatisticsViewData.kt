package com.example.football.domain

import kotlinx.collections.immutable.ImmutableList

data class PlayerStatisticsViewData(
    val playerInfoViewData: PlayerInfoViewData,
    val statisticsViewDate: ImmutableList<StatisticsViewDate>
)

data class PlayerInfoViewData(
    val fullName: String,
    val surname: String,
    val age: Int,
    val weight: String,
    val imageUrl: String,
    val playerRating: Int
)

data class StatisticsViewDate(
    val games: Int,
    val shots: Int,
    val goals: Int,
    val passes: Int,
    val tackles: Int,
    val assists: Int,
    val duelsWon: Int,
    val dribblesCompleted: Int,
    val fouls: Int,
    val redCards: Int,
    val competition: String,
    val competitionImageUrl: String,
    val team: String,
    val teamLogoUrl: String,
    val yellowCards: Int
)
