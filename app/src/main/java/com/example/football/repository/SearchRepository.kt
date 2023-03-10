package com.example.football.repository

import com.example.football.FootballService
import com.example.football.model.CountryData

interface SearchRepository {

    suspend fun searchCountry(search:String): List<CountryData>
}

internal class SearchRepositoryImpl(private val footballService: FootballService) : SearchRepository {
    override suspend fun searchCountry(search: String): List<CountryData> {
        TODO("Not yet implemented")
    }

}