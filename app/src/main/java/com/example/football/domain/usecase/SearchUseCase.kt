package com.example.football.domain.usecase

import com.example.football.data.repository.SearchRepository
import com.example.football.domain.CountryViewData
import com.example.football.domain.LeagueViewData
import com.example.football.domain.SearchDataMapper

interface SearchUseCase {
    suspend fun searchCountry(searchQuery: String): SearchResult
    suspend fun searchLeague(searchQuery: String): SearchResult
}

internal class SearchUseCaseImpl(
    private val searchRepository: SearchRepository,
    private val searchDataMapper: SearchDataMapper
) : SearchUseCase {
    override suspend fun searchCountry(searchQuery: String): SearchResult {
        val result = searchRepository.searchCountry(searchQuery)
        return if (result.isSuccess) {
            val searchResponse = result.getOrNull()
            searchDataMapper.mapCountrySearchResult(searchResponse)
        } else {
            SearchResult.SearchError
        }
    }

    override suspend fun searchLeague(searchQuery: String): SearchResult {
        val result = searchRepository.searchByLeague(searchQuery)
        return if (result.isSuccess) {
            searchDataMapper.mapLeagueSearchResult(result.getOrNull())
        } else {
            SearchResult.SearchError
        }
    }
}

sealed class SearchResult {
    data class LoadedCountries(val countries: List<CountryViewData>) : SearchResult()
    object NoResultsFound : SearchResult()
    object SearchError : SearchResult()
    data class LoadedLeagues(val leagues: List<LeagueViewData>) : SearchResult()
}
