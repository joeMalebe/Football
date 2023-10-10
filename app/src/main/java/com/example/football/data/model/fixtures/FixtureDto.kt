package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FixtureDto(
    @SerialName("date")
    val date: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("periods")
    val periods: PeriodsDto = PeriodsDto(),
    @SerialName("referee")
    val referee: String? = null,
    @SerialName("status")
    val status: StatusDto = StatusDto(),
    @SerialName("timestamp")
    val timestamp: Int = 0,
    @SerialName("timezone")
    val timezone: String = "",
    @SerialName("venue")
    val venue: VenueDto = VenueDto()
)