package com.example.football.domain

import com.example.football.domain.usecase.SearchResult
import com.example.football.domain.usecase.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchDataMapperImplTest {

    private val searchDataMapper = SearchDataMapperImpl()

    @Test
    fun `mapSearchResponse with list of countries should return countries loaded result`() {
        val result = searchDataMapper.mapSearchResponse(TestData.SuccessfulSearchResponse)

        assertTrue(result::class.java === SearchResult.LoadedCountries::class.java)

        val actual = result as SearchResult.LoadedCountries
        assertTrue(actual.countries.size == 3)
        assertEquals("Japan", actual.countries[0].name)
        assertEquals("", actual.countries[0].flagUri)
        assertEquals("", actual.countries[0].code)
    }

    @Test
    fun `mapSearchResponse with empty list of countries should return no result`() {
        val actual = searchDataMapper.mapSearchResponse(TestData.EmptySearchResponse)

        assertTrue(actual::class.java === SearchResult.NoResultsFound::class.java)
    }
}