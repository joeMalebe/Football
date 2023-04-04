package com.example.football

import com.example.football.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballService {

    @GET("countries")
    suspend fun search(@Query("search") search: String): Result<SearchResponse>
}