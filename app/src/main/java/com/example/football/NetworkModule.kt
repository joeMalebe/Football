package com.example.football

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun footballService(okHttpClient: OkHttpClient): FootballService {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
            .create(FootballService::class.java)
    }

    @Provides
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor {
            it.proceed(
                it.request().newBuilder()
                    .url(BuildConfig.BASE_URL)
                    .addHeader("X-RapidAPI-Key", BuildConfig.API_KEY)
                    .addHeader("X-RapidAPI-Host", BuildConfig.BASE_URL)
                    .build()
            )
        }.build()
    }
}