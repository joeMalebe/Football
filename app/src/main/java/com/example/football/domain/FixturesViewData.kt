package com.example.football.domain

data class FixturesViewData(
    val fixtureId: String,
    val homeTeam: TeamFixtureViewData,
    val awayTeam: TeamFixtureViewData,
    val date: String,
    val time: String
)