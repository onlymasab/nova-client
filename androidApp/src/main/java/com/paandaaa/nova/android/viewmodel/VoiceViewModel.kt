package com.paandaaa.nova.android.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.nova.android.domain.repository.VoiceRepository
import com.paandaaa.nova.android.domain.usecase.voice.TranscribeVoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val transcribeUseCase: TranscribeVoiceUseCase
) : ViewModel() {

    var result by mutableStateOf("")
        private set

    fun startListening() {
        viewModelScope.launch {
            result = "Listening..."
            val transcription = transcribeUseCase()
            result = transcription.text
        }
    }
}