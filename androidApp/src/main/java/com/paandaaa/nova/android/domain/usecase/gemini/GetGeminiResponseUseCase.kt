package com.paandaaa.nova.android.domain.usecase.gemini

import com.paandaaa.nova.android.domain.repository.GeminiRepository
import javax.inject.Inject

/**
 * Use case for getting a response from the Gemini model.
 * This class orchestrates the data flow from the repository to the ViewModel.
 * @param repository The GeminiRepository to use for fetching data.
 */
class GetGeminiResponseUseCase @Inject constructor(
    private val repository: GeminiRepository
) {
    /**
     * Executes the use case to get a Gemini response.
     * @param prompt The user's query.
     * @return A String containing the Gemini model's response.
     */
    suspend operator fun invoke(prompt: String): String {
        return repository.getGeminiResponse(prompt)
    }
}
