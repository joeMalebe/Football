package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.CountryLeagueDto
import com.example.football.data.model.SearchResponse
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun searchCountry(search: String): Result<SearchResponse>
    suspend fun searchByLeague(search: String): Result<CountryLeagueDto>
}

internal class SearchRepositoryImpl(
    private val footballService: FootballService,
    private val ioContext: CoroutineContext
) : SearchRepository {
    override suspend fun searchCountry(search: String): Result<SearchResponse> {
        return withContext(ioContext) {
            footballService.searchCountries(search)
        }
    }

    override suspend fun searchByLeague(search: String): Result<CountryLeagueDto> {
        return withContext(ioContext) {
            footballService.searchLeagues(search)
        }
    }
}