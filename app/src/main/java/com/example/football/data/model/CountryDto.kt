package com.example.football.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerialName("name") val name: String = "",
    @SerialName("code") val code: String? = "",
    @SerialName("flag") val flagUrl: String? = ""
)
