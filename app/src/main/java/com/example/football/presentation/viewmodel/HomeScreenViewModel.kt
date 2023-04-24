package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.CountryViewData
import com.example.football.domain.LeagueViewData
import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

private const val MIN_TEXT_LENGTH_FOR_SEARCH = 3

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val ioContext: CoroutineContext
) : ViewModel() {

    private val _searchResultViewState =
        MutableLiveData<SearchViewState>(SearchViewState.InitialViewState)
    val searchResultViewState: LiveData<SearchViewState>
        get() = _searchResultViewState

    private var searchText = "$"
    val searchResults = mutableListOf<CountryViewData>()
    val searchLeagueResults = mutableListOf<LeagueViewData>()

    fun search(searchQuery: String) {
        viewModelScope.launch(context = ioContext) {
            _searchResultViewState.postValue(SearchViewState.Loading)
            when (val result = searchUseCase.searchCountry(searchQuery)) {
                is SearchResult.LoadedCountries -> {
                    searchResults.clear()
                    searchResults.addAll(result.countries)
                    _searchResultViewState.postValue(
                        SearchViewState.CountrySearchResults(result.countries)
                    )
                }
                is SearchResult.NoResultsFound -> {
                    _searchResultViewState.postValue(SearchViewState.NoSearchResults)
                }
                else -> {
                    _searchResultViewState.postValue(SearchViewState.Error)
                }
            }
        }
    }

    fun searchOnTextChanged(searchText: String) {
        if (isValidSearchText(searchText)) {
            this.searchText = searchText.substring(0..2)
            search(searchText)
        } else if (searchResults.isNotEmpty() && searchText.contains(this.searchText, true)) {
            _searchResultViewState.postValue(
                SearchViewState.CountrySearchResults(
                    searchResults.filter { country ->
                        country.name.contains(
                            searchText,
                            true
                        )
                    }
                )
            )
        }
    }

    private fun isValidSearchText(searchText: String) =
        searchText.length >= MIN_TEXT_LENGTH_FOR_SEARCH && !searchText.startsWith(
            this.searchText,
            true
        )

    fun searchLeague(searchQuery: String) {
        viewModelScope.launch(ioContext) {
            _searchResultViewState.postValue(SearchViewState.Loading)
            when (val result = searchUseCase.searchLeague(searchQuery)) {
                SearchResult.SearchError -> {
                    _searchResultViewState.postValue(SearchViewState.Error)
                }
                is SearchResult.LoadedLeagues -> {
                    searchLeagueResults.clear()
                    searchLeagueResults.addAll(result.leagues)
                    _searchResultViewState.postValue(
                        SearchViewState.LeagueSearchResults(result.leagues)
                    )
                }
                else -> {
                    _searchResultViewState.postValue(SearchViewState.NoSearchResults)
                }
            }
        }
    }

    fun searchLeagueOnTextChanged(searchQuery: String) {
        if (searchQuery.length >= MIN_TEXT_LENGTH_FOR_SEARCH && !searchQuery
                .contains(searchText, true)
        ) {
            searchText = searchQuery.substring(0..2)
            searchLeague(searchQuery)
        } else if (searchQuery.contains(searchText, true)) {
            _searchResultViewState.postValue(SearchViewState.Loading)

            val filteredResults = filterResultsByLeagueOrCountryName(searchQuery)
            _searchResultViewState.postValue(updateViewState(filteredResults))
        }
    }

    private fun filterResultsByLeagueOrCountryName(searchQuery: String) =
        searchLeagueResults.filter {
            it.name.contains(
                searchQuery,
                true
            ) || it.country.name.contains(searchQuery, true)
        }

    private fun updateViewState(filteredResults: List<LeagueViewData>): SearchViewState {
        return if (filteredResults.isEmpty()) {
            SearchViewState.NoSearchResults
        } else {
            SearchViewState.LeagueSearchResults(filteredResults)
        }
    }

    sealed class SearchViewState {

        object Loading : SearchViewState()

        object Error : SearchViewState()

        object NoSearchResults : SearchViewState()

        object InitialViewState : SearchViewState()

        data class CountrySearchResults(val countries: List<CountryViewData>) : SearchViewState()

        data class LeagueSearchResults(val leagues: List<LeagueViewData>) : SearchViewState()
    }
}
