package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FixtureResponseDto(
    @SerialName("response")
    val response: List<ResponseDto> = listOf()
)