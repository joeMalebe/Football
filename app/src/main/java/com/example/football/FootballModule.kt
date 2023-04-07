package com.example.football

import com.example.football.data.repository.SearchRepository
import com.example.football.data.repository.SearchRepositoryImpl
import com.example.football.domain.SearchDataMapper
import com.example.football.domain.SearchDataMapperImpl
import com.example.football.domain.usecase.SearchCountryUseCase
import com.example.football.domain.usecase.SearchCountryUseCaseImpl
import com.example.football.presentation.viewmodel.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
class FootballModule {

    @Provides
    fun getSearchRepository(
        footballService: FootballService,
        ioContext: CoroutineContext
    ): SearchRepository {
        return SearchRepositoryImpl(footballService, ioContext)
    }

    @Provides
    fun getHomeScreenViewModel(
        searchCountryUseCase: SearchCountryUseCase,
        ioContext: CoroutineContext
    ): HomeScreenViewModel {
        return HomeScreenViewModel(searchCountryUseCase, ioContext)
    }

    @Provides
    fun getSearchUseCase(
        searchRepository: SearchRepository,
        searchDataMapper: SearchDataMapper
    ): SearchCountryUseCase {
        return SearchCountryUseCaseImpl(searchRepository, searchDataMapper)
    }

    @Provides
    fun getSearchMapper(): SearchDataMapper {
        return SearchDataMapperImpl()
    }
}