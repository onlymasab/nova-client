package com.paandaaa.nova.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paandaaa.nova.android.domain.usecase.VoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val useCase: VoiceUseCase
) : ViewModel() {

    private val _result = MutableStateFlow("Say 'Hey Nova' to start")
    val result: StateFlow<String> = _result

    fun startListening() {
        viewModelScope.launch {
            useCase.listen().collectLatest { speech ->
                _result.value = speech
            }
        }
    }
}