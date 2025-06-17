package com.paandaaa.nova.android.domain.usecase

import com.paandaaa.nova.android.domain.repository.VoiceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VoiceUseCase @Inject constructor(
    private val repository: VoiceRepository
) {
    fun listen(): Flow<String> = repository.listenAndRecognize()
}