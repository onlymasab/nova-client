package com.paandaaa.nova.android.domain.repository


/**
 * Interface defining the contract for interacting with Gemini data.
 * This abstracts away the data source (remote, local).
 */
interface GeminiRepository {
    /**
     * Gets a response from the Gemini model based on a given prompt.
     * @param prompt The user's query.
     * @return A String containing the Gemini model's response.
     */
    suspend fun getGeminiResponse(prompt: String): String
}
