package com.example.football.domain.usecase

import com.example.football.data.model.CountryData
import com.example.football.data.model.SearchResponse
import com.example.football.data.repository.SearchRepository
import com.example.football.domain.CountryViewData
import com.example.football.domain.SearchDataMapper
import com.example.football.domain.usecase.TestData.SuccessfulSearchResponse
import com.example.football.domain.usecase.TestData.countries
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCountryUseCaseImplTest {

    @Mock
    lateinit var searchRepository: SearchRepository

    @Mock
    lateinit var searchDataMapper: SearchDataMapper

    lateinit var searchCountryUseCase: SearchCountryUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchCountryUseCase = SearchCountryUseCaseImpl(searchRepository, searchDataMapper)
    }

    @Test
    fun `When response is success execute should return countries loaded result`() = runTest {
        val expectedResult = SearchResult.LoadedCountries(countries)
        whenever(searchRepository.searchCountry(any())).thenReturn(
            Result.success(SuccessfulSearchResponse)
        )
        whenever(searchDataMapper.mapSearchResponse(SuccessfulSearchResponse)).thenReturn(
            expectedResult
        )

        val actualResult = searchCountryUseCase.execute("Jam")

        assertSame(expectedResult, actualResult)
        verify(searchRepository).searchCountry("Jam")
        verify(searchDataMapper).mapSearchResponse(SuccessfulSearchResponse)
    }

    @Test
    fun `When response is failure execute should return search error result`() = runTest {
        val expectedResult = SearchResult.SearchError
        whenever(searchRepository.searchCountry(any())).thenReturn(Result.failure(Throwable()))

        val actualResult = searchCountryUseCase.execute("Jam")

        assertSame(expectedResult, actualResult)
        verify(searchRepository).searchCountry("Jam")
        verifyNoInteractions(searchDataMapper)
    }
}

object TestData {
    val SuccessfulSearchResponse = SearchResponse(
        arrayListOf(
            CountryData(name = "Japan"),
            CountryData(name = "Jamaica"),
            CountryData(name = "Jordan")
        )
    )
    val EmptySearchResponse = SearchResponse(
        arrayListOf()
    )
    val countries = arrayListOf(
        CountryViewData(name = "Japan", flagUri = "url", code = "2"),
        CountryViewData(name = "Jamaica", flagUri = "url", code = "6"),
        CountryViewData(name = "Jordan", flagUri = "url", code = "4")
    )
}