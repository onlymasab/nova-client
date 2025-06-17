package com.paandaaa.nova.android.domain.repository

import com.paandaaa.nova.android.domain.model.TranscriptionModel
import kotlinx.coroutines.flow.Flow

interface VoiceRepository {
    /**
     * Starts voice transcription and returns a flow of ongoing transcription results
     * including partial and final transcriptions
     */
    suspend fun startVoiceTranscription(): Flow<TranscriptionResult>

    /**
     * Stops the ongoing voice transcription and returns the final result
     */
    suspend fun stopVoiceTranscription(): TranscriptionModel

    /**
     * Checks if voice transcription is currently active
     */
    fun isTranscribing(): Boolean

    /**
     * Saves the transcription to persistent storage
     */
    suspend fun saveTranscription(transcription: TranscriptionModel)

    /**
     * Retrieves transcription history
     */
    fun getTranscriptionHistory(): Flow<List<TranscriptionModel>>

    sealed class TranscriptionResult {
        data class Partial(val text: String) : TranscriptionResult()
        data class Final(val transcription: TranscriptionModel) : TranscriptionResult()
        data class Error(val exception: Exception) : TranscriptionResult()
    }
}