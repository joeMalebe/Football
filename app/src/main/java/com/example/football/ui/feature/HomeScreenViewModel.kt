package com.example.football.ui.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.football.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val searchRepository: SearchRepository, private val ioContext: CoroutineContext) : ViewModel() {
    fun initialise() {
        CoroutineScope(ioContext).launch {
            val response = searchRepository.searchCountry("engl")
            Log.d("HomeScreenViewModel", response.toString())
        }
        Log.d("HomeScreenViewModel","hello view model")
    }
    // todo: Implement the ViewModel
}