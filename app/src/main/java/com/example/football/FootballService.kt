package com.example.football

import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.model.SearchLeagueResponse
import com.example.football.data.model.SearchResponse
import com.example.football.data.model.StandingsResponse
import com.example.football.data.model.TopScorersResponse
import com.example.football.data.model.fixtures.FixtureDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FootballService {

    @GET("countries")
    suspend fun searchCountries(@Query("search") search: String): Result<SearchResponse>

    @GET("leagues")
    suspend fun searchLeagues(@Query("search") search: String): Result<SearchLeagueResponse>

    @GET("standings")
    suspend fun getStandings(
        @Query("league") leagueId: String,
        @Query("season") season: String
    ): Result<StandingsResponse>

    @GET("players/topscorers")
    suspend fun getTopScorers(
        @Query("league") leagueId: String,
        @Query("season") season: String
    ): Result<TopScorersResponse>

    @GET("players")
    suspend fun getPlayer(
        @Query("id") playerId: String,
        @Query("season") season: String
    ): Result<PlayerStatisticsDto>

    @GET("fixtures")
    suspend fun getFixtures(
        @Query("team") teamId: String,
        @Query("season") season: String,
        @Query("next") next: Int
    ): Result<FixtureDto>
}
