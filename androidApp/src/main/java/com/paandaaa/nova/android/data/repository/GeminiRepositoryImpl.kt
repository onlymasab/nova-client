package com.paandaaa.nova.android.data.repository

import com.paandaaa.nova.android.BuildConfig
import com.paandaaa.nova.android.data.remote.api.GeminiApiService
import com.paandaaa.nova.android.domain.model.GeminiRequest
import com.paandaaa.nova.android.domain.model.Message
import com.paandaaa.nova.android.domain.model.Part
import com.paandaaa.nova.android.domain.repository.GeminiRepository
import javax.inject.Inject

/**
 * Implementation of GeminiRepository that uses GeminiApiService to fetch data from the remote source.
 * @param geminiApiService An instance of GeminiApiService provided by Hilt.
 */
class GeminiRepositoryImpl @Inject constructor(
    private val geminiApiService: GeminiApiService
) : GeminiRepository {

    // IMPORTANT: Replace with your actual Gemini API Key.
    // This key is used directly in the repository for now.
    // For production, consider using BuildConfig or a more secure method.
    private val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY // <<< REPLACE WITH YOUR API KEY >>>


    /**
     * Gets a response from the Gemini model based on a given prompt.
     * Constructs the Gemini API request with the user prompt and a "Nova" persona.
     * @param prompt The user's query.
     * @return A String containing the Gemini model's response.
     * @throws Exception if the API call fails or response is empty.
     */
    override suspend fun getGeminiResponse(prompt: String): String {
        // Embed the "Nova" persona directly into the user prompt for Gemini to understand.
        // This makes the model act as Nova.
        // Added instruction to keep the response concise and under ~500 words.
        val novaPrompt = "You are Nova, an AI assistant designed to help the user. " +
                "Respond in a helpful, friendly, and concise manner, directly answering the user's question. " +
                "Keep your response short and to the point, ideally under 500 words. " +
                "Do not introduce yourself in every response. Your persona is a helpful AI named Nova. " +
                "User query: $prompt"

        val request = GeminiRequest(
            contents = listOf(
                Message(
                    role = "user",
                    parts = listOf(Part(text = novaPrompt))
                )
            )
        )
        val response = geminiApiService.generateContent(
            request = request,
            apiKey = GEMINI_API_KEY
        )

        // Extract the first candidate's first part's text, if available.
        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Gemini API response was empty or malformed.")
    }
}