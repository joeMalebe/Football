package com.example.football.domain

import com.example.football.data.model.CountryDto
import com.example.football.data.model.SearchLeagueResponse
import com.example.football.data.model.SearchResponse
import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.SearchResult.LoadedCountries
import com.example.football.domain.usecase.SearchResult.LoadedLeagues
import com.example.football.domain.usecase.SearchResult.NoResultsFound

interface SearchDataMapper {
    fun mapCountrySearchResult(searchResponse: SearchResponse?): SearchResult
    fun mapLeagueSearchResult(searchLeagueResponse: SearchLeagueResponse?): SearchResult
}

internal class SearchDataMapperImpl : SearchDataMapper {
    override fun mapCountrySearchResult(searchResponse: SearchResponse?): SearchResult {
        return if (searchResponse == null || searchResponse.response.isEmpty()) {
            NoResultsFound
        } else {
            val countries = searchResponse.response.map {
                mapDtoToCountryViewData(it)
            }
            LoadedCountries(countries)
        }
    }

    override fun mapLeagueSearchResult(searchLeagueResponse: SearchLeagueResponse?): SearchResult {
        return if (searchLeagueResponse == null || searchLeagueResponse.response.isEmpty()) {
            NoResultsFound
        } else {
            LoadedLeagues(
                searchLeagueResponse.response.map { dto ->
                    LeagueViewData(
                        dto.league.id,
                        dto.league.name,
                        dto.league.logo,
                        mapDtoToCountryViewData(dto.country)
                    )
                }
            )
        }
    }

    private fun mapDtoToCountryViewData(country: CountryDto) =
        CountryViewData(country.name, country.flagUrl ?: "", country.code ?: "")
}
