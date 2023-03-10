package com.example.football.ui.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    fun initialise() {
        Log.d("HomeScreenViewModel","hello view model")
    }
    // TODO: Implement the ViewModel
}