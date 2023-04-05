package com.example.football.domain.usecase

import com.example.football.data.repository.SearchRepository
import com.example.football.domain.CountryViewData
import com.example.football.domain.SearchDataMapper

interface SearchCountryUseCase {
    suspend fun execute(searchQuery: String): SearchResult
}

internal class SearchCountryUseCaseImpl(
    private val searchRepository: SearchRepository,
    private val searchDataMapper: SearchDataMapper
) : SearchCountryUseCase {
    override suspend fun execute(searchQuery: String): SearchResult {
        val result = searchRepository.searchCountry(searchQuery)
        return if (result.isSuccess) {
            val searchResponse = result.getOrNull()
            searchDataMapper.mapSearchResponse(searchResponse)
        } else {
            SearchResult.SearchError
        }
    }
}

sealed class SearchResult {
    data class LoadedCountries(val countries: List<CountryViewData>) : SearchResult()
    object NoResultsFound : SearchResult()
    object SearchError : SearchResult()
}
