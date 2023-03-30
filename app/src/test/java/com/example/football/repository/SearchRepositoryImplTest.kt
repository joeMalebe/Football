package com.example.football.repository

import com.example.football.FootballService
import com.example.football.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    @Mock
    private lateinit var footballService: FootballService

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        searchRepository = SearchRepositoryImpl(
            footballService = footballService,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun searchCountry() = runTest {
        whenever(footballService.search(any())).thenReturn(Result.success(SearchResponse()))

        searchRepository.searchCountry("hello")

        verify(footballService).search(any())
    }
}