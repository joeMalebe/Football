package com.example.football

import com.example.football.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballService {

    @GET(value = "/v3/countries?search")
    suspend fun search(@Query("search") search: String) : Result<SearchResponse>

}