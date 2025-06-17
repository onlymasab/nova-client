package com.paandaaa.nova.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface VoiceRepository {
    fun listenAndRecognize(): Flow<String>
}