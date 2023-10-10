package com.example.football.data.model.fixtures
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScoreDto(
    @SerialName("extratime")
    val extratime: ExtratimeDto = ExtratimeDto(),
    @SerialName("fulltime")
    val fulltime: FulltimeDto = FulltimeDto(),
    @SerialName("halftime")
    val halftime: HalftimeDto = HalftimeDto(),
    @SerialName("penalty")
    val penalty: PenaltyDto = PenaltyDto()
)