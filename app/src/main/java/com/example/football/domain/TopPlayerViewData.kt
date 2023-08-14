package com.example.football.domain

data class TopPlayerViewData(
    val playerId: String,
    val playerName: String,
    val playerSurname: String,
    val teamName: String,
    val numberOfGoals: Int,
    val playerImgUrl: String
) {
    val playerFullName: String
        get() = "$playerName $playerSurname"
}