package com.paandaaa.nova.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NovaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Vosk model
    }
}