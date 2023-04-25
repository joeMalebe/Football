package com.example.football.domain

data class StandingsViewData(
    val position: Int,
    val teamId: Int,
    val logo: String,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val points: Int,
    val positionDescription: String?
)