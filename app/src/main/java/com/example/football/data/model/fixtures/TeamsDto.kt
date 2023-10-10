package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamsDto(
    @SerialName("away")
    val away: AwayDto = AwayDto(),
    @SerialName("home")
    val home: HomeDto = HomeDto()
)