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

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val searchCountryUseCase: SearchCountryUseCase,
    private val ioContext: CoroutineContext
) : ViewModel() {

    private val _searchResultSearchViewState = MutableLiveData<SearchViewState>()
    val searchResultSearchViewState: LiveData<SearchViewState>
        get() = _searchResultSearchViewState

    fun search(searchQuery: String) {
        viewModelScope.launch(context = ioContext) {
            _searchResultSearchViewState.postValue(SearchViewState.Loading)
            when (val result = searchCountryUseCase.execute(searchQuery)) {
                is SearchResult.LoadedCountries -> {
                    _searchResultSearchViewState.postValue(
                        SearchViewState.SearchResults(result.countries)
                    )
                }
                is SearchResult.NoResultsFound -> {
                    _searchResultSearchViewState.postValue(SearchViewState.NoSearchResults)
                }
                else -> {
                    _searchResultSearchViewState.postValue(SearchViewState.Error)
                }
            }
        }
    }

    sealed class SearchViewState {

        object Loading : SearchViewState()

        object Error : SearchViewState()

        object NoSearchResults : SearchViewState()

        data class SearchResults(val countries: List<CountryViewData>) : SearchViewState()
    }
}