package com.example.football.domain.usecase

import com.example.football.data.model.*
import com.example.football.data.repository.SearchRepository
import com.example.football.domain.CountryViewData
import com.example.football.domain.LeagueViewData
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

    @Test
    fun `When search for league fails return error`() = runTest {
        val searchQuery = "sea"
        whenever(searchRepository.searchByLeague(searchQuery)).thenReturn(
            Result.failure(Throwable())
        )

        val sut = searchUseCase.searchLeague(searchQuery)

        assertSame(sut, SearchResult.SearchError)
        verify(searchRepository).searchByLeague(searchQuery)
        verifyNoInteractions(searchDataMapper)
    }

    @Test
    fun `When search for leagues is successful then return loaded leagues`() = runTest {
        val searchQuery = "search"
        val expected = SearchResult.LoadedLeagues(TestData.leaguesViewData)
        whenever(searchRepository.searchByLeague(searchQuery)).thenReturn(
            Result.success(TestData.searchLeagueResponse)
        )
        whenever(searchDataMapper.mapLeagueSearchResult(TestData.searchLeagueResponse)).thenReturn(
            expected
        )

        val sut = searchUseCase.searchLeague(searchQuery)

        assertSame(sut, expected)
        verify(searchRepository).searchByLeague(searchQuery)
        verify(searchDataMapper).mapLeagueSearchResult(TestData.searchLeagueResponse)
    }

    @Test
    fun `When search for leagues is successful with empty list return no results`() = runTest {
        val searchQuery = "search"
        val expected = SearchResult.NoResultsFound
        whenever(searchDataMapper.mapLeagueSearchResult(TestData.emptySearchLeagueResponse))
            .thenReturn(
                expected
            )
        whenever(searchRepository.searchByLeague(searchQuery)).thenReturn(
            Result.success(TestData.emptySearchLeagueResponse)
        )

        val sut = searchUseCase.searchLeague(searchQuery)

        assertSame(sut, expected)
        verify(searchRepository).searchByLeague(searchQuery)
        verify(searchDataMapper).mapLeagueSearchResult(TestData.emptySearchLeagueResponse)
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

    val countryLeagueDtos = listOf(
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

    val leaguesViewData = listOf(
        LeagueViewData(
            1,
            "Premier League",
            "https://example.com/premier-league.png",
            CountryViewData("England", "https://example.com/england-flag.png", "ENG")
        ),

        LeagueViewData(
            2,
            "La Liga",
            "https://example.com/la-liga.png",
            CountryViewData("Spain", "https://example.com/spain-flag.png", "ESP")
        ),

        LeagueViewData(
            3,
            "Bundesliga",
            "https://example.com/bundesliga.png",
            CountryViewData("Germany", "https://example.com/germany-flag.png", "GER")
        ),

        LeagueViewData(
            4,
            "Serie A",
            "https://example.com/serie-a.png",
            CountryViewData("Italy", "https://example.com/italy-flag.png", "ITA")
        ),

        LeagueViewData(
            5,
            "Ligue 1",
            "https://example.com/ligue-1.png",
            CountryViewData("France", "https://example.com/france-flag.png", "FRA")
        ),

        LeagueViewData(
            6,
            "Eredivisie",
            "https://example.com/eredivisie.png",
            CountryViewData("Netherlands", "https://example.com/netherlands-flag.png", "NED")
        )
    )
    val searchLeagueResponse = SearchLeagueResponse(countryLeagueDtos)
}
