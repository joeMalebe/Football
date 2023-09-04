package com.example.football

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.football.data.model.PlayerStatisticsDto
import com.example.football.exception.ResultCallAdaptorFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit

@RunWith(RobolectricTestRunner::class)
class FootballServiceTest {

    private var mockService = MockWebServer()

    private lateinit var service: FootballService
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val context: Context = ApplicationProvider.getApplicationContext()

    @OptIn(ExperimentalSerializationApi::class)
    @Before
    fun setup() {
        mockService.start()
        val contentType = "application/json".toMediaType()

        service = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(OkHttpClient())
            .addCallAdapterFactory(ResultCallAdaptorFactory())
            .baseUrl(mockService.url("/"))
            .build()
            .create(FootballService::class.java)
    }

    @After
    fun tearDown() {
        mockService.shutdown()
    }

    @Test
    fun `player_stats_response json decodes to PlayerStatisticsDto`() {
        val resourceId = R.raw.player_stats_response
        val playerStatisticsDto = readJsonFromRawResource(context, resourceId)

        assertEquals(1, playerStatisticsDto?.response?.size)
        assertEquals(4, playerStatisticsDto?.response?.first()?.statistics?.size)
        val player = playerStatisticsDto?.response?.first()!!.player
        assertEquals(29, player.age)
        assertEquals("https://media.api-sports.io/football/players/276.png", player.photo)
        assertEquals("Neymar", player.name)
        assertEquals("Neymar", player.firstname)
        assertEquals("da Silva Santos Júnior", player.lastname)
        assertEquals(276, player.id)
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