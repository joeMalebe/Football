package com.example.football.data.repository

import com.example.football.FootballService
import com.example.football.data.model.SearchResponse
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun searchCountry(search: String): Result<SearchResponse>
}

internal class SearchRepositoryImpl(
    private val footballService: FootballService,
    private val ioContext: CoroutineContext
) : SearchRepository {
    override suspend fun searchCountry(search: String): Result<SearchResponse> {
        return withContext(ioContext) {
            footballService.search(search)
        }
    }
}