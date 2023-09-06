package com.example.football.domain

import kotlinx.collections.immutable.PersistentList

data class PlayerStatisticsViewData(
    private val playerInfoViewData: PlayerInfoViewData,
    private val statisticsViewDate: PersistentList<StatisticsViewDate>
)

data class PlayerInfoViewData(
    private val fullName: String,
    private val surname: String,
    private val age: Int,
    private val weight: String,
    private val imageUrl: String,
    private val playerRating: Int,
)

data class StatisticsViewDate(
    private val games: Int,
    private val shots: Int,
    private val goals: Int,
    private val passes: Int,
    private val tackles: Int,
    private val assists: Int,
    private val duelsWon: Int,
    private val dribblesCompleted: Int,
    private val fouls: Int,
    private val redCards: Int,
    private val competition: String,
    private val competitionImageUrl: String,
    private val team: String,
    private val teamLogoUrl: String,
    private val yellowCards: Int,
)
