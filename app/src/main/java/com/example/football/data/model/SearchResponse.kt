package com.example.football.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("response") val response: ArrayList<CountryDto> = arrayListOf()
)
