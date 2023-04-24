package com.example.football.repository

import com.example.football.FootballService
import com.example.football.data.model.SearchLeagueResponse
import com.example.football.data.model.SearchResponse
import com.example.football.data.repository.SearchRepository
import com.example.football.data.repository.SearchRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    @Mock
    private lateinit var footballService: FootballService

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        searchRepository = SearchRepositoryImpl(
            footballService = footballService,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun searchCountry() = runTest {
        whenever(footballService.searchCountries(any())).thenReturn(
            Result.success(SearchResponse())
        )

        searchRepository.searchCountry("hello")

        verify(footballService).searchCountries(any())
    }

    @Test
    fun searchByLeague() = runTest {
        val searchQuery = "Premier"
        whenever(footballService.searchLeagues(searchQuery)).thenReturn(
            Result.success(
                SearchLeagueResponse()
            )
        )

        searchRepository.searchByLeague(searchQuery)

        verify(footballService).searchLeagues(searchQuery)
    }

    @Test
    fun `When search by league throws an exception then return failure result`() = runTest {
        whenever(footballService.searchLeagues("eng")).thenThrow(
            HttpException(Response.error<SearchLeagueResponse>(500, "no internet".toResponseBody()))
        )

        val sut = searchRepository.searchByLeague("eng")

        val exception = sut.exceptionOrNull() as HttpException
        assertTrue(sut.isFailure)
        assertSame(HttpException::class.java, exception::class.java)
    }
}
