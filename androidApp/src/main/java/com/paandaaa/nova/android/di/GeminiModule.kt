package com.paandaaa.nova.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.paandaaa.nova.android.data.remote.api.GeminiApiService
import com.paandaaa.nova.android.data.repository.GeminiRepositoryImpl
import com.paandaaa.nova.android.domain.repository.GeminiRepository
import com.paandaaa.nova.android.domain.usecase.gemini.GeminiUseCases
import com.paandaaa.nova.android.domain.usecase.gemini.GetGeminiResponseUseCase
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(GeminiApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideGeminiApiService(
        retrofit: Retrofit
    ): GeminiApiService = retrofit.create(GeminiApiService::class.java)

    @Provides
    @Singleton
    fun provideGeminiRepository(
        geminiApiService: GeminiApiService
    ): GeminiRepository = GeminiRepositoryImpl(geminiApiService)

    @Provides
    @Singleton
    fun provideGetGeminiResponseUseCase(
        geminiRepository: GeminiRepository
    ): GetGeminiResponseUseCase = GetGeminiResponseUseCase(geminiRepository)

    @Provides
    @Singleton
    fun provideGeminiUseCases(
        getGeminiResponseUseCase: GetGeminiResponseUseCase
    ): GeminiUseCases = GeminiUseCases(getGeminiResponseUseCase)
}