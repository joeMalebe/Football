package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchLeagueResponse(
    @SerialName("response") val response: List<CountryLeagueDto> = listOf()
)

@Serializable
data class CountryLeagueDto(
    @SerialName("league") val league: LeagueDto = LeagueDto(),
    @SerialName("country") val country: CountryDto = CountryDto()
)