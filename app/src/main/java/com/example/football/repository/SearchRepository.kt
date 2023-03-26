package com.example.football.repository

import com.example.football.FootballService
import com.example.football.model.SearchResponse
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

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