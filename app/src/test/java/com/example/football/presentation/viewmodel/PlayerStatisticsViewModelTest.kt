package com.example.football.presentation.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.football.FootballService
import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.data.repository.PlayerStatisticsRepositoryImpl
import com.example.football.domain.usecase.PlayerStatisticsUseCaseImpl
import com.example.football.exception.ResultCallAdaptorFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations.openMocks
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSerializationApi::class)
class PlayerStatisticsViewModelTest {

    private lateinit var footballService: FootballService
    private val playerStatisticsRepository by lazy { PlayerStatisticsRepositoryImpl(footballService) }
    private val playerStatisticsUseCase by lazy {
        PlayerStatisticsUseCaseImpl(
            playerStatisticsRepository
        )
    }
    private val testDispatcher = UnconfinedTestDispatcher()
    private val okHttpClient = OkHttpClient()
    private val mockWebServer = MockWebServer()
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val playerStatisticsViewModel: PlayerStatisticsViewModel by lazy {
        PlayerStatisticsViewModel(playerStatisticsUseCase, testDispatcher)
    }

    @Before
    fun setUp() {
        openMocks(this)
        mockWebServer.start()
        footballService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType = "application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdaptorFactory())
            .client(okHttpClient)
            .build()
            .create(FootballService::class.java)

    }


    @Test
    fun `When player stats loads player data successfully then return success view state`() {

    }

    private fun readJsonFromRawResource(context: Context, resourceId: Int): PlayerStatisticsDto? {
        return try {
            val inputStream = context.resources.openRawResource(resourceId)
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            json.decodeFromString<PlayerStatisticsDto>(jsonText)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}