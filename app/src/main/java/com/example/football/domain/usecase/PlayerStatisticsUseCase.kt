package com.example.football.domain.usecase

import com.example.football.data.repository.PlayerStatisticsRepository

interface PlayerStatisticsUseCase {

}
internal class PlayerStatisticsUseCaseImpl(private val playerStatisticsRepository: PlayerStatisticsRepository) :
    PlayerStatisticsUseCase{

}


