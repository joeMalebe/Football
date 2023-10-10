package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusDto(
    @SerialName("elapsed")
    val elapsed: Int? = null,
    @SerialName("long")
    val long: String = "",
    @SerialName("short")
    val short: String = ""
)