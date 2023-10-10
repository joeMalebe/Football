package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AwayDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("logo")
    val logo: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("winner")
    val winner: Boolean? = null
)