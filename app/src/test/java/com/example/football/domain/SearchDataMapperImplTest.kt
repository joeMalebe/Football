package com.example.football.domain

import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchDataMapperImplTest {

    private val searchDataMapper = SearchDataMapperImpl()

    @Test
    fun `mapSearchResponse with list of countries should return countries loaded result`() {
        val result = searchDataMapper.mapCountrySearchResult(TestData.successfulSearchResponse)

        assertTrue(result::class.java === SearchResult.LoadedCountries::class.java)

        val actual = result as SearchResult.LoadedCountries
        assertTrue(actual.countries.size == 3)
        assertEquals("Japan", actual.countries[0].name)
        assertEquals("", actual.countries[0].flagUri)
        assertEquals("", actual.countries[0].code)
    }

    @Test
    fun `mapSearchResponse with empty list of countries should return no result`() {
        val actual = searchDataMapper.mapCountrySearchResult(TestData.emptySearchResponse)

        assertTrue(actual::class.java === SearchResult.NoResultsFound::class.java)
    }

    @Test
    fun `Given mapLoadedLeagues with populated data should return Loaded Sons`() {
        val sut = searchDataMapper.mapLeagueSearchResult(TestData.searchLeagueResponse)

        assertTrue(sut::class.java === SearchResult.LoadedLeagues::class.java)
        val leagues = sut as SearchResult.LoadedLeagues
        assertTrue(leagues.leagueViewData.size == 6)
        assertLeagueViewData(
            1,
            "Premier League",
            "https://example.com/premier-league.png",
            CountryViewData("England", "https://example.com/england-flag.png", "ENG"),
            leagues.leagueViewData[0]
        )

        assertLeagueViewData(
            2,
            "La Liga",
            "https://example.com/la-liga.png",
            CountryViewData("Spain", "https://example.com/spain-flag.png", "ESP"),
            leagues.leagueViewData[1]
        )

        assertLeagueViewData(
            3,
            "Bundesliga",
            "https://example.com/bundesliga.png",
            CountryViewData("Germany", "https://example.com/germany-flag.png", "GER"),
            leagues.leagueViewData[2]
        )

        assertLeagueViewData(
            4,
            "Serie A",
            "https://example.com/serie-a.png",
            CountryViewData("Italy", "https://example.com/italy-flag.png", "ITA"),
            leagues.leagueViewData[3]
        )

        assertLeagueViewData(
            5,
            "Ligue 1",
            "https://example.com/ligue-1.png",
            CountryViewData("France", "https://example.com/france-flag.png", "FRA"),
            leagues.leagueViewData[4]
        )

        assertLeagueViewData(
            6,
            "Eredivisie",
            "https://example.com/eredivisie.png",
            CountryViewData("Netherlands", "https://example.com/netherlands-flag.png", "NED"),
            leagues.leagueViewData[5]
        )
    }

    @Test
    fun `When country league list is empty then return no results`() {
        val sut = searchDataMapper.mapLeagueSearchResult(emptyList())

        assertTrue(sut::class.java === SearchResult.NoResultsFound::class.java)
    }

    private fun assertLeagueViewData(
        expectedId: Int,
        expectedName: String,
        expectedLogo: String,
        expectedCountry: CountryViewData,
        actual: LeagueViewData
    ) {
        assertEquals(expectedId, actual.id)
        assertEquals(expectedName, actual.name)
        assertEquals(expectedLogo, actual.logo)
        assertCountryViewData(
            expectedCountry.name,
            expectedCountry.flagUri,
            expectedCountry.code,
            actual.country
        )
    }

    private fun assertCountryViewData(
        expectedName: String,
        expectedFlagUri: String,
        expectedCode: String,
        actual: CountryViewData
    ) {
        assertEquals(expectedName, actual.name)
        assertEquals(expectedFlagUri, actual.flagUri)
        assertEquals(expectedCode, actual.code)
    }
}