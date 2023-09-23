package com.example.football.domain

data class FixtureResultViewData(
    val fixtureId: String,
    val homeTeam: TeamFixtureViewData,
    val awayTeam: TeamFixtureViewData,
    val date: String,
    val gameState: String
)

data class TeamFixtureViewData(
    val teamName: String,
    val teamLogoUrl: String,
    val goals: String,
    val teamId: String
)