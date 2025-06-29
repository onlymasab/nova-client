package com.paandaaa.nova.android.data.remote.api

import com.paandaaa.nova.android.domain.model.GeminiRequest
import com.paandaaa.nova.android.domain.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApiService {

    companion object {
        const val BASE_URL = "https://generativelanguage.googleapis.com/"
        const val DEFAULT_MODEL = "gemini-2.0-flash"
    }

    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String = DEFAULT_MODEL,
        @Body request: GeminiRequest,
        @Query("key") apiKey: String
    ): GeminiResponse
}