package com.paandaaa.nova.android.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.nova.android.domain.usecase.gemini.GeminiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed class VoiceState {
    object Idle : VoiceState()
    object Listening : VoiceState()
    data class Result(val text: String) : VoiceState()
    object ProcessingResponse : VoiceState()
    data class Speaking(val text: String) : VoiceState()
    data class Error(val message: String) : VoiceState()
}

@HiltViewModel
class VoiceViewModel @Inject constructor(
    application: Application,
    private val geminiUseCases: GeminiUseCases
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val state: StateFlow<VoiceState> = _state

    private val context = application.applicationContext
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private var textToSpeech: TextToSpeech? = null

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale.getDefault()
                val result = textToSpeech?.setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported: $locale")
                    _state.value = VoiceState.Error("TTS language not supported.")
                } else {
                    setFemaleVoice(textToSpeech, locale)
                }
            } else {
                Log.e("TTS", "TTS init failed: $status")
                _state.value = VoiceState.Error("TTS initialization failed.")
            }
        }
    }

    private fun setFemaleVoice(tts: TextToSpeech?, locale: Locale) {
        tts?.let {
            val femaleVoice = it.voices.firstOrNull { voice ->
                voice.locale == locale && voice.name.contains("female", ignoreCase = true)
            } ?: it.voices.firstOrNull { voice ->
                voice.locale == locale && (voice.name.endsWith("#f") || voice.name.endsWith("-f", ignoreCase = true))
            } ?: it.voices.firstOrNull { voice ->
                voice.locale == locale
            }

            femaleVoice?.let { voice ->
                val result = it.setVoice(voice)
                if (result == TextToSpeech.SUCCESS) {
                    Log.d("TTS", "✅ Female voice set: ${voice.name}")
                } else {
                    Log.e("TTS", "❌ Failed to set voice")
                }
            } ?: Log.w("TTS", "⚠️ No matching female voice found")
        }
    }

    fun startListening() {
        if (_state.value is VoiceState.Listening) return

        _state.value = VoiceState.Listening
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}

            override fun onResults(results: Bundle?) {
                val recognizedText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                if (!recognizedText.isNullOrBlank()) {
                    _state.value = VoiceState.Result(recognizedText)
                    processSpeechWithGemini(recognizedText)
                } else {
                    _state.value = VoiceState.Error("No speech recognized.")
                    restart()
                }
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
                    SpeechRecognizer.ERROR_CLIENT -> "Client error."
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions."
                    SpeechRecognizer.ERROR_NETWORK -> "Network error."
                    SpeechRecognizer.ERROR_NO_MATCH -> "No match found."
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy."
                    SpeechRecognizer.ERROR_SERVER -> "Server error."
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout."
                    else -> "Unknown error: $error"
                }
                Log.e("Voice", "Speech error: $errorMessage")
                _state.value = VoiceState.Error(errorMessage)
                speakResponse("I'm sorry, there was an issue recognizing your voice.")
                restart()
            }

            override fun onEndOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizer.startListening(intent)
    }

    private fun processSpeechWithGemini(query: String) {
        viewModelScope.launch {
            _state.value = VoiceState.ProcessingResponse
            try {
                val geminiResponse = geminiUseCases.getGeminiResponseUseCase(query)
                Log.d("Gemini", "Gemini response: $geminiResponse")
                speakResponse(geminiResponse)
            } catch (e: Exception) {
                val errorMsg = "Failed to get Gemini response: ${e.message}"
                Log.e("Gemini", errorMsg, e)
                _state.value = VoiceState.Error(errorMsg)
                speakResponse("Sorry, I couldn't respond right now.")
                restart()
            }
        }
    }

    private fun speakResponse(text: String) {
        textToSpeech?.let { tts ->
            if (tts.isSpeaking) tts.stop()

            _state.value = VoiceState.Speaking(text)

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    restart()
                }

                override fun onError(utteranceId: String?) {
                    _state.value = VoiceState.Error("TTS error")
                    restart()
                }
            })

            val params = Bundle()
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "nova-utterance")
        } ?: run {
            Log.e("TTS", "TTS not ready")
            _state.value = VoiceState.Error("TTS not initialized.")
            restart()
        }
    }

    private fun restart() {
        viewModelScope.launch {
            _state.value = VoiceState.Idle
        }
    }

    override fun onCleared() {
        super.onCleared()
        recognizer.destroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
}