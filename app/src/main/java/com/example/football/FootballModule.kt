package com.example.football

import com.example.football.data.repository.SearchRepository
import com.example.football.data.repository.SearchRepositoryImpl
import com.example.football.domain.SearchDataMapper
import com.example.football.domain.SearchDataMapperImpl
import com.example.football.domain.usecase.SearchUseCase
import com.example.football.domain.usecase.SearchUseCaseImpl
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
        searchUseCase: SearchUseCase,
        ioContext: CoroutineContext
    ): HomeScreenViewModel {
        return HomeScreenViewModel(searchUseCase, ioContext)
    }

    @Provides
    fun getSearchUseCase(
        searchRepository: SearchRepository,
        searchDataMapper: SearchDataMapper
    ): SearchUseCase {
        return SearchUseCaseImpl(searchRepository, searchDataMapper)
    }

    @Provides
    fun getSearchMapper(): SearchDataMapper {
        return SearchDataMapperImpl()
    }
}