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

    lateinit var viewModel: HomeScreenViewModel

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
        viewModel.searchResultSearchViewState.observeForever(observer)

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
        viewModel.searchResultSearchViewState.observeForever(observer)

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
        viewModel.searchResultSearchViewState.observeForever(observer)

        viewModel.search("search text")

        argumentCaptor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(
                listOf(SearchViewState.Loading, SearchViewState.Error),
                allValues
            )
        }
    }
}

object TestData {
    val countries = arrayListOf(
        CountryViewData(name = "Japan", flagUri = "url", code = "2"),
        CountryViewData(name = "Jamaica", flagUri = "url", code = "6"),
        CountryViewData(name = "Jordan", flagUri = "url", code = "4")
    )
}