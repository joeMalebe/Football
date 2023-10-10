package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PeriodsDto(
    @SerialName("first")
    val first: Int? = null,
    @SerialName("second")
    val second: Int? = null
)