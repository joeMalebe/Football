package com.example.football.data.repository

import com.example.football.FootballService

interface PlayerStatisticsRepository {

}

internal class PlayerStatisticsRepositoryImpl(
    private val footballService: FootballService
) : PlayerStatisticsRepository {

}
