package com.example.football.domain.usecase

import com.example.football.data.model.*
import com.example.football.data.repository.SearchRepository
import com.example.football.domain.CountryViewData
import com.example.football.domain.SearchDataMapper
import com.example.football.domain.usecase.TestData.countries
import com.example.football.domain.usecase.TestData.successfulSearchResponse
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchUseCaseImplTest {

    @Mock
    lateinit var searchRepository: SearchRepository

    @Mock
    lateinit var searchDataMapper: SearchDataMapper

    lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchUseCase = SearchUseCaseImpl(searchRepository, searchDataMapper)
    }

    @Test
    fun `When response is success execute should return countries loaded result`() = runTest {
        val expectedResult = SearchResult.LoadedCountries(countries)
        whenever(searchRepository.searchCountry(any())).thenReturn(
            Result.success(successfulSearchResponse)
        )
        whenever(searchDataMapper.mapCountrySearchResult(successfulSearchResponse)).thenReturn(
            expectedResult
        )

        val actualResult = searchUseCase.searchCountry("Jam")

        assertSame(expectedResult, actualResult)
        verify(searchRepository).searchCountry("Jam")
        verify(searchDataMapper).mapCountrySearchResult(successfulSearchResponse)
    }

    @Test
    fun `When response is failure execute should return search error result`() = runTest {
        val expectedResult = SearchResult.SearchError
        whenever(searchRepository.searchCountry(any())).thenReturn(Result.failure(Throwable()))

        val actualResult = searchUseCase.searchCountry("Jam")

        assertSame(expectedResult, actualResult)
        verify(searchRepository).searchCountry("Jam")
        verifyNoInteractions(searchDataMapper)
    }
}

object TestData {

    val successfulSearchResponse = SearchResponse(
        arrayListOf(
            CountryDto(name = "Japan"),
            CountryDto(name = "Jamaica"),
            CountryDto(name = "Jordan")
        )
    )

    val emptySearchResponse = SearchResponse(arrayListOf())

    val emptySearchLeagueResponse = SearchLeagueResponse(emptyList())

    val countries = arrayListOf(
        CountryViewData(name = "Japan", flagUri = "url", code = "2"),
        CountryViewData(name = "Jamaica", flagUri = "url", code = "6"),
        CountryViewData(name = "Jordan", flagUri = "url", code = "4")
    )

    val searchLeagueResponse = listOf(
        CountryLeagueDto(
            LeagueDto(1, "Premier League", "soccer", "https://example.com/premier-league.png"),
            CountryDto("England", "ENG", "https://example.com/england-flag.png")
        ),
        CountryLeagueDto(
            LeagueDto(2, "La Liga", "soccer", "https://example.com/la-liga.png"),
            CountryDto("Spain", "ESP", "https://example.com/spain-flag.png")
        ),
        CountryLeagueDto(
            LeagueDto(3, "Bundesliga", "soccer", "https://example.com/bundesliga.png"),
            CountryDto("Germany", "GER", "https://example.com/germany-flag.png")
        ),
        CountryLeagueDto(
            LeagueDto(4, "Serie A", "soccer", "https://example.com/serie-a.png"),
            CountryDto("Italy", "ITA", "https://example.com/italy-flag.png")
        ),
        CountryLeagueDto(
            LeagueDto(5, "Ligue 1", "soccer", "https://example.com/ligue-1.png"),
            CountryDto("France", "FRA", "https://example.com/france-flag.png")
        ),
        CountryLeagueDto(
            LeagueDto(6, "Eredivisie", "soccer", "https://example.com/eredivisie.png"),
            CountryDto("Netherlands", "NED", "https://example.com/netherlands-flag.png")
        )
    )
}