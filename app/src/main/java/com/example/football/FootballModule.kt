package com.example.football

import com.example.football.repository.SearchRepository
import com.example.football.repository.SearchRepositoryImpl
import com.example.football.ui.feature.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FootballModule {

    @Provides
    fun getSearchRepository(footballService: FootballService): SearchRepository {
        return SearchRepositoryImpl(footballService)
    }

    @Provides
    fun getHomeScreenViewModel(): HomeScreenViewModel {
        return HomeScreenViewModel()
    }
}