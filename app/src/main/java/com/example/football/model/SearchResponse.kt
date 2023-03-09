package com.example.football.model
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(val response: ArrayList<CountryData>)
