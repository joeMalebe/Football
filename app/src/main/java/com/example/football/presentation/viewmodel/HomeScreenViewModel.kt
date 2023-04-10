package com.example.football.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.football.domain.CountryViewData
import com.example.football.domain.usecase.SearchCountryUseCase
import com.example.football.domain.usecase.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.launch

private const val MIN_TEXT_LENGTH_FOR_SEARCH = 3

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val searchCountryUseCase: SearchCountryUseCase,
    private val ioContext: CoroutineContext
) : ViewModel() {

    private val _searchResultViewState = MutableLiveData<SearchViewState>()
    val searchResultViewState: LiveData<SearchViewState>
        get() = _searchResultViewState

    private var searchText = "$"
    val searchResults = mutableListOf<CountryViewData>()

    fun search(searchQuery: String) {
        viewModelScope.launch(context = ioContext) {
            _searchResultViewState.postValue(SearchViewState.Loading)
            when (val result = searchCountryUseCase.execute(searchQuery)) {
                is SearchResult.LoadedCountries -> {
                    searchResults.clear()
                    searchResults.addAll(result.countries)
                    _searchResultViewState.postValue(
                        SearchViewState.SearchResults(result.countries)
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
        } else if (searchResults.isNotEmpty() && searchText.startsWith(this.searchText)) {
            _searchResultViewState.postValue(
                SearchViewState.SearchResults(
                    searchResults.filter { country ->
                        country.name.startsWith(
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
            false
        )

    sealed class SearchViewState {

        object Loading : SearchViewState()

        object Error : SearchViewState()

        object NoSearchResults : SearchViewState()

        data class SearchResults(val countries: List<CountryViewData>) : SearchViewState()
    }
}