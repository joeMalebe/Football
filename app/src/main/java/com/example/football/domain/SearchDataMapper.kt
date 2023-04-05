package com.example.football.domain

import com.example.football.data.model.SearchResponse
import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.SearchResult.LoadedCountries
import com.example.football.domain.usecase.SearchResult.NoResultsFound

interface SearchDataMapper {
    fun mapSearchResponse(searchResponse: SearchResponse?): SearchResult
}

internal class SearchDataMapperImpl : SearchDataMapper {
    override fun mapSearchResponse(searchResponse: SearchResponse?): SearchResult {
        return if (searchResponse == null || searchResponse.response.isEmpty()) {
            NoResultsFound
        } else {
            val countries = searchResponse.response.map {
                CountryViewData(it.name, it.flagUrl, it.code)
            }
            LoadedCountries(countries)
        }
    }
}