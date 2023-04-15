package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchLeagueResponse(
    @SerialName("league") val league: LeagueDto = LeagueDto(),
    @SerialName("country") val country: CountryData = CountryData()
)