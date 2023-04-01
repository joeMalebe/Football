package com.example.football.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("response") val response: ArrayList<CountryData> = arrayListOf()
)
