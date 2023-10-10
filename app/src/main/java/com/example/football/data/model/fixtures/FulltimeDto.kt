package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FulltimeDto(
    @SerialName("away")
    val away: Int? = null,
    @SerialName("home")
    val home: Int? = null
)