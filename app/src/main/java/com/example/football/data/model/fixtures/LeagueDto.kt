package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LeagueDto(
    @SerialName("country")
    val country: String = "",
    @SerialName("flag")
    val flag: String? = null,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("logo")
    val logo: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("round")
    val round: String = "",
    @SerialName("season")
    val season: Int = 0
)