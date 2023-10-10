package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VenueDto(
    @SerialName("city")
    val city: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = ""
)