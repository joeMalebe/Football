package com.example.football.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.football.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val ioContext: CoroutineContext
) : ViewModel() {
    fun initialise() {
        CoroutineScope(ioContext).launch {
            val response = searchRepository.searchCountry("engl")
            Log.d("HomeScreenViewModel", response.toString())
        }
        Log.d("HomeScreenViewModel", "hello view model")
    }
    // todo: Implement the ViewModel
}