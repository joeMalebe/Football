package com.example.football.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CountryData(
    @SerialName("name") val name: String = "",
    @SerialName("code") val code: String = "",
    @SerialName("flag") val flagUrl: String = ""
)
