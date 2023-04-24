package com.example.football.domain

data class LeagueViewData(
    val id: Int,
    val name: String,
    val logo: String,
    val country: CountryViewData
)
