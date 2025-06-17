package com.paandaaa.nova.android.domain.usecase.voice

import com.paandaaa.nova.android.domain.model.TranscriptionModel
import com.paandaaa.nova.android.domain.repository.VoiceRepository
import javax.inject.Inject

class TranscribeVoiceUseCase @Inject constructor(
    private val repository: VoiceRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isTranscribing()
    }
}