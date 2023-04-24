package com.example.football

import com.example.football.exception.ResultCallAdaptorFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun footballService(okHttpClient: OkHttpClient): FootballService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .addCallAdapterFactory(ResultCallAdaptorFactory())
            .baseUrl("https://" + BuildConfig.BASE_URL)
            .build()
            .create(FootballService::class.java)
    }

    @Provides
    fun okHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient().newBuilder().addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
                    .addHeader("x-rapidapi-host", BuildConfig.BASE_URL)
                    .build()
            )
        }.addInterceptor(interceptor).build()
    }

    @Provides
    fun ioContext(): CoroutineContext {
        return Dispatchers.IO
    }
}
