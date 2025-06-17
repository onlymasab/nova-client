package com.paandaaa.nova.android.domain.model

/**
 * Represents a voice transcription result with metadata
 * @property text Recognized speech text
 * @property timestamp When the transcription was created (millis since epoch)
 * @property isFinal Whether this is a final transcription (true) or partial/interim result (false)
 * @property confidence Confidence score (0.0-1.0) of the recognition accuracy
 * @property language Language code of the transcription (e.g., "en-US")
 * @property sessionId Unique ID grouping transcriptions from the same listening session
 */
data class TranscriptionModel(
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFinal: Boolean = true,
    val confidence: Float? = null,
    val language: String = "en-US",
    val sessionId: String = generateSessionId()
) {
    companion object {
        /**
         * Generates a unique session ID using UUID and current time
         */
        fun generateSessionId(): String {
            return "session_${System.currentTimeMillis()}_${java.util.UUID.randomUUID().toString().take(8)}"
        }
    }

    /**
     * Creates a copy with marked as final transcription
     */
    fun asFinal() = copy(isFinal = true)

    /**
     * Creates a copy with marked as partial transcription
     */
    fun asPartial() = copy(isFinal = false)
}