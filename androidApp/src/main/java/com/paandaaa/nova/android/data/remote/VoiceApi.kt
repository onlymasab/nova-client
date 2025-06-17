package com.paandaaa.nova.android.data.remote

interface VoiceApi {
    suspend fun transcribe(): String // Simulate or wrap Whisper API
}