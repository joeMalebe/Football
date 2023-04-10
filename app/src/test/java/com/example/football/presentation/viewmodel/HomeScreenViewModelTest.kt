package com.example.football.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.football.domain.CountryViewData
import com.example.football.domain.usecase.SearchCountryUseCase
import com.example.football.domain.usecase.SearchResult
import com.example.football.presentation.viewmodel.HomeScreenViewModel.SearchViewState
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
    lateinit var searchCountryUseCase: SearchCountryUseCase

    private lateinit var viewModel: HomeScreenViewModel

    @Mock
    lateinit var observer: Observer<SearchViewState>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeScreenViewModel(searchCountryUseCase, Unconfined)
    }

    @Test
    fun `search should first load then return search results`() = runTest {
        whenever(searchCountryUseCase.execute(any())).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                listOf(
                    SearchViewState.Loading,
                    SearchViewState.SearchResults(TestData.countries)
                ),
                allValues
            )
        }
    }

    @Test
    fun `search should first load then return no search results`() = runTest {
        whenever(searchCountryUseCase.execute(any())).thenReturn(SearchResult.NoResultsFound)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                listOf(SearchViewState.Loading, SearchViewState.NoSearchResults),
                allValues
            )
        }
    }

    @Test
    fun `search should first load then return error`() = runTest {
        whenever(searchCountryUseCase.execute(any())).thenReturn(SearchResult.SearchError)
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                listOf(SearchViewState.Loading, SearchViewState.Error),
                allValues
            )
        }
    }

    @Test
    fun `search should execute when 3 or more characters are inputted`() = runTest {
        viewModel.searchOnTextChanged(searchText = "country")

        verify(searchCountryUseCase).execute("country")
    }

    @Test
    fun `search should not execute if text is less than 3 characters`() = runTest {
        viewModel.searchOnTextChanged(searchText = "co")

        verifyNoInteractions(searchCountryUseCase)
    }

    @Test
    fun `search should not execute if text is the same as previous search`() = runTest {
        var searchText = "chars"
        for (i in 1..5) {
            viewModel.searchOnTextChanged(searchText)
            searchText = searchText.substring(0, searchText.length - 1)
        }

        verify(searchCountryUseCase, times(1)).execute(any())
    }

    @Test
    fun `search should execute when text is different from previous search`() = runTest {
        viewModel.searchOnTextChanged("first search")
        viewModel.searchOnTextChanged("second search")
        viewModel.searchOnTextChanged("third search")
        viewModel.searchOnTextChanged("fourth search")
        viewModel.searchOnTextChanged("fov")

        verify(searchCountryUseCase, times(5)).execute(any())
    }

    @Test
    fun `search results should be empty on init`() {
        val results = viewModel.searchResults

        assertTrue(results.isEmpty())
    }

    @Test
    fun `after search is successful search results is not empty`() = runTest {
        whenever(searchCountryUseCase.execute("sea")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        val argumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchOnTextChanged("sea")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(SearchViewState.Loading, this.firstValue)
            val searchResultsViewState = this.secondValue as SearchViewState.SearchResults
            assertEquals(searchResultsViewState.countries[0], viewModel.searchResults[0])
        }
    }

    @Test
    fun `after 2 searches is successful search results is updated`() = runTest {
        whenever(searchCountryUseCase.execute("sea")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries
            )
        )
        whenever(searchCountryUseCase.execute("germ")).thenReturn(
            SearchResult.LoadedCountries(
                TestData.countries2
            )
        )
        val firstSearchArgumentCaptor = argumentCaptor<SearchViewState>()
        val secondSearchArgumentCaptor = argumentCaptor<SearchViewState>()
        viewModel.searchResultViewState.observeForever(observer)

        viewModel.searchOnTextChanged("sea")
        firstSearchArgumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(SearchViewState.Loading, this.firstValue)
            val searchResultsViewState = this.secondValue as SearchViewState.SearchResults
            assertEquals(searchResultsViewState.countries[0], viewModel.searchResults[0])
        }

        viewModel.searchOnTextChanged("germ")

        secondSearchArgumentCaptor.run {
            verify(observer, times(4)).onChanged(capture())
            assertEquals(SearchViewState.Loading, this.thirdValue)
            val newSearchResultsView = this.lastValue as SearchViewState.SearchResults
            assertEquals(newSearchResultsView.countries[0], viewModel.searchResults[0])
        }
    }

    @Test
    fun `search results should be filtered by prefix when search text is similar`() = runTest {
        whenever(searchCountryUseCase.execute("port")).thenReturn(
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
            verify(observer, times(4)).onChanged(capture())
            assertEquals(SearchViewState.Loading, this.firstValue)
            val firstResult = this.secondValue as SearchViewState.SearchResults
            val secondResult = this.thirdValue as SearchViewState.SearchResults
            val thirdResult = this.lastValue as SearchViewState.SearchResults

            assertTrue(firstResult.countries.size == 4)
            assertTrue(secondResult.countries.size == 2)
            assertTrue(thirdResult.countries.size == 1)
        }
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
        CountryViewData(name = "Port", flagUri = "url", code = "2"),
        CountryViewData(name = "Portugal", flagUri = "url", code = "6"),
        CountryViewData(name = "Porto", flagUri = "url", code = "4"),
        CountryViewData(name = "Porto Allegra", flagUri = "url", code = "4")
    )
}