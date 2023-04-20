package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.domain.CountryViewData
import com.example.football.domain.LeagueViewData
import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.SearchUseCase
import com.example.football.presentation.viewmodel.HomeScreenViewModel.SearchViewState
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    @Mock
    lateinit var searchUseCase: SearchUseCase

    private lateinit var viewModel: HomeScreenViewModel

    @Mock
    lateinit var observer: Observer<SearchViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeScreenViewModel(searchUseCase, Unconfined)
    }

    @After
    fun removeObserver() {
        viewModel.searchResultViewState.removeObserver(observer)
    }

    @Test
    fun `when initialised the state should be default view`() {
        assertEquals(SearchViewState.InitialViewState, viewModel.searchResultViewState.value)
    }

    @Test
    fun `search should first load then return search results`() = runTest {
        whenever(searchUseCase.searchCountry(any())).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(
                listOf(
                    SearchViewState.InitialViewState,
                    SearchViewState.Loading,
                    SearchViewState.CountrySearchResults(TestData.countries)
                ),
                allValues
            )
        }
    }

    @Test
    fun `search should first load then return no search results`() = runTest {
        whenever(searchUseCase.searchCountry(any())).thenReturn(SearchResult.NoResultsFound)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(
                listOf(
                    SearchViewState.InitialViewState,
                    SearchViewState.Loading,
                    SearchViewState.NoSearchResults
                ),
                allValues
            )
        }
    }

    @Test
    fun `search should first load then return error`() = runTest {
        whenever(searchUseCase.searchCountry(any())).thenReturn(SearchResult.SearchError)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(
                listOf(
                    SearchViewState.InitialViewState,
                    SearchViewState.Loading,
                    SearchViewState.Error
                ),
                allValues
            )
        }
    }

    @Test
    fun `search should execute when 3 or more characters are inputted`() = runTest {
        viewModel.searchOnTextChanged(searchText = "country")

        verify(searchUseCase).searchCountry("country")
    }

    @Test
    fun `search should not execute if text is less than 3 characters`() = runTest {
        viewModel.searchOnTextChanged(searchText = "co")

        verifyNoInteractions(searchUseCase)
    }

    @Test
    fun `search should not execute if text is the same as previous search`() = runTest {
        var searchText = "chars"
        for (i in 1..5) {
            viewModel.searchOnTextChanged(searchText)
            searchText = searchText.substring(0, searchText.length - 1)
        }

        verify(searchUseCase, times(1)).searchCountry(any())
    }

    @Test
    fun `search should execute when text is different from previous search`() = runTest {
        viewModel.searchOnTextChanged("first search")
        viewModel.searchOnTextChanged("second search")
        viewModel.searchOnTextChanged("third search")
        viewModel.searchOnTextChanged("fourth search")
        viewModel.searchOnTextChanged("fov")

        verify(searchUseCase, times(5)).searchCountry(any())
    }

    @Test
    fun `search results should be empty on init`() {
        val results = viewModel.searchResults

        assertTrue(results.isEmpty())
    }

    @Test
    fun `after search is successful search results is not empty`() = runTest {
        whenever(searchUseCase.searchCountry("sea")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchOnTextChanged("sea")

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(SearchViewState.InitialViewState, this.firstValue)
            assertEquals(SearchViewState.Loading, this.secondValue)
            val searchResultsViewState = this.thirdValue as SearchViewState.CountrySearchResults
            assertEquals(searchResultsViewState.countries[0], viewModel.searchResults[0])
        }
    }

    @Test
    fun `after 2 searches is successful search results is updated`() = runTest {
        whenever(searchUseCase.searchCountry("sea")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        whenever(searchUseCase.searchCountry("germ")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries2
            )
        )
        val firstSearchArgumentCaptor = argumentCaptor<SearchViewState>()
        val secondSearchArgumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchOnTextChanged("sea")
        firstSearchArgumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(SearchViewState.InitialViewState, this.firstValue)
            assertEquals(SearchViewState.Loading, this.secondValue)
            val countrySearchResultsViewState = this.thirdValue as SearchViewState.CountrySearchResults
            assertEquals(countrySearchResultsViewState.countries[0], viewModel.searchResults[0])
        }

        viewModel.searchOnTextChanged("germ")

        secondSearchArgumentCaptor.run {
            verify(observer, times(5)).onChanged(capture())
            val newCountrySearchResultsView = this.lastValue as SearchViewState.CountrySearchResults
            assertEquals(newCountrySearchResultsView.countries[0], viewModel.searchResults[0])
        }
    }

    @Test
    fun `results should be filtered by countries the contain the same search text in the name`() =
        runTest {
            whenever(searchUseCase.searchCountry("port")).thenReturn(
                SearchResult.LoadedCountries(
                    TestData.countries3
                )
            )
            val argumentCaptor = argumentCaptor<SearchViewState>()
            viewModel.searchResultViewState.observeForever(observer)

            viewModel.searchOnTextChanged("port")
            viewModel.searchOnTextChanged("porto")
            viewModel.searchOnTextChanged("portu")

            argumentCaptor.run {
                verify(observer, times(5)).onChanged(capture())
                val allStates = this.allValues
                assertEquals(SearchViewState.InitialViewState, allStates[0])
                assertEquals(SearchViewState.Loading, allStates[1])
                val firstResult = allStates[2] as SearchViewState.CountrySearchResults
                val secondResult = allStates[3] as SearchViewState.CountrySearchResults
                val thirdResult = allStates[4] as SearchViewState.CountrySearchResults
                assertTrue(firstResult.countries.size == 4)
                assertTrue(secondResult.countries.size == 2)
                assertTrue(thirdResult.countries.size == 1)
            }
        }

    @Test
    fun `When search is failed emit error view state`() = runTest {
        val searchQuery = "search"
        whenever(searchUseCase.searchLeague(searchQuery)).thenReturn(SearchResult.SearchError)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchLeague(searchQuery)

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            verify(searchUseCase).searchLeague(searchQuery)
            assertEquals(firstValue, SearchViewState.InitialViewState)
            assertEquals(secondValue, SearchViewState.Loading)
            assertEquals(thirdValue, SearchViewState.Error)
        }
    }

    @Test
    fun `When search leagues returns no results emit no results view state`() = runTest {
        val searchQuery = "search"
        whenever(searchUseCase.searchLeague(searchQuery)).thenReturn(SearchResult.NoResultsFound)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchLeague(searchQuery)

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            verify(searchUseCase).searchLeague(searchQuery)
            assertEquals(firstValue, SearchViewState.InitialViewState)
            assertEquals(secondValue, SearchViewState.Loading)
            assertEquals(thirdValue, SearchViewState.NoSearchResults)
        }
    }

    @Test
    fun `When search leagues returns search results emit search results view state`() = runTest {
        val searchQuery = "search"
        whenever(searchUseCase.searchLeague(searchQuery)).thenReturn(SearchResult.LoadedLeagues(TestData.leaguesViewData))
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchLeague(searchQuery)

        argumentCaptor.run {
            verify(observer, times(3)).onChanged(capture())
            verify(searchUseCase).searchLeague(searchQuery)
            assertEquals(firstValue, SearchViewState.InitialViewState)
            assertEquals(secondValue, SearchViewState.Loading)
            assertEquals(thirdValue, SearchViewState.LeagueSearchResults(TestData.leaguesViewData))
        }
    }

    @Test
    fun `When search query is less than 3 chars then don't call the search league use case`() = runTest {
        val searchQuery = "Se"

        viewModel.searchLeagueOnTextChanged(searchQuery)

        verifyNoInteractions(searchUseCase)
    }

    @Test
    fun `When search query is 3 or more chars then call the search league use case`() = runTest {
        val searchQuery = "Sea"

        viewModel.searchLeagueOnTextChanged(searchQuery)

        verify(searchUseCase).searchLeague(searchQuery)
    }
}

object TestData {
    val countries = arrayListOf(
        CountryViewData(name = "Japan", flagUri = "url", code = "2"),
        CountryViewData(name = "Jamaica", flagUri = "url", code = "6"),
        CountryViewData(name = "Jordan", flagUri = "url", code = "4")
    )

    val countries2 = arrayListOf(
        CountryViewData(name = "Germany", flagUri = "url", code = "8"),
        CountryViewData(name = "Ghana", flagUri = "url", code = "12")
    )

    val countries3 = arrayListOf(
        CountryViewData(name = "Elportinose", flagUri = "url", code = "2"),
        CountryViewData(name = "Portugal", flagUri = "url", code = "6"),
        CountryViewData(name = "Porto", flagUri = "url", code = "4"),
        CountryViewData(name = "Vahporto Allegra", flagUri = "url", code = "4")
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
}