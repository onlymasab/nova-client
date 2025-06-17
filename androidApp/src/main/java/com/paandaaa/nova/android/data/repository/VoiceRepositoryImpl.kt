package com.paandaaa.nova.android.data.repository

import com.paandaaa.nova.android.data.local.dao.ConversationDao
import com.paandaaa.nova.android.data.local.entity.ConversationEntity
import com.paandaaa.nova.android.data.remote.VoiceApi
import com.paandaaa.nova.android.domain.model.TranscriptionModel
import com.paandaaa.nova.android.domain.repository.VoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class VoiceRepositoryImpl @Inject constructor(
    private val api: VoiceApi,
    private val dao: ConversationDao
) : VoiceRepository {
    suspend fun transcribeVoice(): TranscriptionModel {
        val text = api.transcribe() // TensorFlow or Whisper logic here
        val result = TranscriptionModel(text)
        dao.insert(ConversationEntity(text = text, timestamp = result.timestamp))
        return result
    }

    override suspend fun startVoiceTranscription(): Flow<VoiceRepository.TranscriptionResult> {
        TODO("Not yet implemented")
    }

    override suspend fun stopVoiceTranscription(): TranscriptionModel {
        TODO("Not yet implemented")
    }

    override fun isTranscribing(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveTranscription(transcription: TranscriptionModel) {
        TODO("Not yet implemented")
    }

    override fun getTranscriptionHistory(): Flow<List<TranscriptionModel>> {
        TODO("Not yet implemented")
    }
}