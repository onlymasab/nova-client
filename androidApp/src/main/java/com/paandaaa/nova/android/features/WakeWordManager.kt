package com.paandaaa.nova.android.features


import ai.picovoice.porcupine.PorcupineManager
import android.content.Context

class WakeWordManager(
    context: Context,
    onWakeWordDetected: () -> Unit
) {
    private val porcupineManager: PorcupineManager = PorcupineManager.Builder()
        .setKeywordPath("hey_nova.ppn")
        .setSensitivity(0.7f)
        .build(context) {
            onWakeWordDetected()
        }

    fun start() = porcupineManager.start()
    fun stop() = porcupineManager.stop()
}