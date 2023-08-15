package com.example.football.domain

data class TopGoalScorerViewData(
    val playerId: Int,
    val playerName: String,
    val playerSurname: String,
    val numberOfGoals: Int,
    val playerImgUrl: String
) {
    val playerFullName: String
        get() = "$playerName $playerSurname"
}