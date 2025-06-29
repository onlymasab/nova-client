package com.paandaaa.nova.android.domain.model

import kotlinx.serialization.Serializable

/**
 * Data class representing a part of the content in a Gemini message.
 * @property text The actual text content.
 */
@Serializable
data class Part(
    val text: String
)

/**
 * Data class representing content within a Gemini message.
 * @property parts A list of parts that make up the content.
 * @property role The role of the content (e.g., "user", "model").
 */
@Serializable
data class Content(
    val parts: List<Part>,
    val role: String
)

/**
 * Data class representing a candidate response from the Gemini model.
 * @property content The content of the candidate response.
 */
@Serializable
data class Candidate(
    val content: Content
)

/**
 * Data class representing the overall response from the Gemini API.
 * @property candidates A list of candidate responses.
 */
@Serializable
data class GeminiResponse(
    val candidates: List<Candidate>
)

/**
 * Data class representing a message to be sent in the Gemini request.
 * @property role The role of the message (e.g., "user").
 * @property parts A list of parts that make up the message content.
 */
@Serializable
data class Message(
    val role: String,
    val parts: List<Part>
)

/**
 * Data class representing the full request payload for the Gemini API.
 * @property contents A list of messages (chat history) to send to the model.
 */
@Serializable
data class GeminiRequest(
    val contents: List<Message>
)